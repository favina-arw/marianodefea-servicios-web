package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.dto.AnomaliaDiariaDTO;
import com.marianodefea.servicios_web.dto.AsistenciaPorAgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.AgenteCargo;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import com.marianodefea.servicios_web.utils.Asistencia;
import com.marianodefea.servicios_web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InformeService {
    @Autowired
    private IAgenteRepository agenteRepository;

    @Autowired
    private IFichadaRepository fichadaRepository;

    @Autowired
    private EventoService eventoService;
    @Autowired
    private AgenteCargoService agenteCargoService;

    @Transactional(readOnly = true)
    public List<AsistenciaPorAgenteDTO> generarInformeAsistenciaMensual(){
        int mesAnterior = LocalDate.now().minusMonths(1).getMonthValue();
        return generarInformeAsistenciaMensual(mesAnterior);
    }

    @Transactional(readOnly = true)
    public List<AsistenciaPorAgenteDTO> generarInformeAsistenciaMensual(int mes) {
        // Calculamos el rango de fechas para el mes y el año actual
        int anioActual = LocalDate.now().getYear();
        LocalDate inicio = LocalDate.of(anioActual, mes, 1);
        LocalDate fin = YearMonth.of(anioActual, mes).atEndOfMonth();

        List<LocalDate> diasHabiles = DateUtils.getDiasLaborablesPorRango(inicio, fin);

        List<Agente> agentes = agenteRepository.findAll();
        List<AsistenciaPorAgenteDTO> informe = new ArrayList<>();

        // 1. OPTIMIZACIÓN: Fichadas en memoria
        LocalDateTime inicioMes = inicio.atStartOfDay();
        LocalDateTime finMes = fin.atTime(LocalTime.MAX);
        List<Fichada> fichadasDelMes = fichadaRepository.findByHoraBetween(inicioMes, finMes);
        Map<Long, List<Fichada>> fichadasPorAgente = fichadasDelMes.stream()
                .filter(f -> f.getAgente() != null)
                .collect(Collectors.groupingBy(f -> f.getAgente().getId()));

        // 2. OPTIMIZACIÓN: Cargos en memoria
        List<AgenteCargo> todosCargos = agenteCargoService.findAll();
        Map<Long, List<AgenteCargo>> cargosPorAgente = todosCargos.stream()
                .collect(Collectors.groupingBy(c -> c.getAgente().getId()));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Agente agente : agentes) {
            List<Asistencia> asistencias = new ArrayList<>();
            List<AnomaliaDiariaDTO> anomalias = new ArrayList<>();

            List<Fichada> misFichadas = fichadasPorAgente.getOrDefault(agente.getId(), new ArrayList<>());
            Map<LocalDate, List<Fichada>> misFichadasPorDia = misFichadas.stream()
                    .collect(Collectors.groupingBy(f -> f.getHora().toLocalDate()));

            List<AgenteCargo> misCargos = cargosPorAgente.getOrDefault(agente.getId(), new ArrayList<>());

            for (LocalDate dia : diasHabiles) {
                String estado = "";

                // Evaluamos cuáles de los cargos del agente estaban activos EXACTAMENTE en este día
                List<AgenteCargo> cargosDelDia = misCargos.stream()
                        .filter(c -> c.getHorario() != null)
                        .filter(c -> c.getFechaAlta() != null && !c.getFechaAlta().isAfter(dia))
                        .filter(c -> {
                            if (c.isActivo()) {
                                return true;
                            } else {
                                return c.getFechaBaja() != null && !c.getFechaBaja().isBefore(dia);
                            }
                        })
                        .collect(Collectors.toList());

                if (dia.isAfter(LocalDate.now())) {
                    estado = "-";
                } else if (!eventoService.esDiaLaboral(dia)) {
                    estado = "F";
                } else if (cargosDelDia.isEmpty()) {
                    estado = "-";
                } else {
                    // CLAVE: Declaramos y extraemos las fichadas del día del mapa en memoria
                    List<Fichada> fichadasHoy = misFichadasPorDia.getOrDefault(dia, new ArrayList<>());

                    if (fichadasHoy.isEmpty()) {
                        estado = "A";
                    } else {
                        estado = "P";

                        // 1. Separamos las fichadas del día por TIPO explícitamente (Ignorando mayúsculas/espacios)
                        List<Fichada> entradasHoy = fichadasHoy.stream()
                                .filter(f -> f.getTipoFichada() != null && f.getTipoFichada().getNombre().toUpperCase().contains("ENTRADA"))
                                .sorted(Comparator.comparing(Fichada::getHora))
                                .collect(Collectors.toList());

                        List<Fichada> salidasHoy = fichadasHoy.stream()
                                .filter(f -> f.getTipoFichada() != null && f.getTipoFichada().getNombre().toUpperCase().contains("SALIDA"))
                                .sorted(Comparator.comparing(Fichada::getHora))
                                .collect(Collectors.toList());

                        // 2. LA JERARQUÍA CORRECTA: Evaluamos Cargo por Cargo (Obligación por Obligación)
                        for (AgenteCargo asignacion : cargosDelDia) {

                            // Evitamos NullPointerExceptions si algún cargo se guardó mal
                            if (asignacion.getHorario() == null || asignacion.getHorario().getHoraInicio() == null || asignacion.getHorario().getHoraFin() == null) {
                                continue;
                            }

                            LocalTime ingEsperado = asignacion.getHorario().getHoraInicio();
                            LocalTime salEsperada = asignacion.getHorario().getHoraFin();

                            // Obtenemos el nombre del cargo para el reporte
                            String nombreCargo = asignacion.getCargo().getCargoTipo().getNombre();

                            // --- EVALUACIÓN DE LA ENTRADA DE ESTE CARGO ---
// Buscamos la entrada que no esté a más de 3 horas (180 min) de diferencia de lo esperado, y nos quedamos con la más cercana.
                            Optional<LocalTime> mejorEntrada = entradasHoy.stream()
                                    .map(f -> f.getHora().toLocalTime())
                                    .filter(hora -> Math.abs(ChronoUnit.MINUTES.between(hora, ingEsperado)) <= 180)
                                    .min(Comparator.comparingLong(h -> Math.abs(ChronoUnit.MINUTES.between(h, ingEsperado))));

                            if (mejorEntrada.isPresent()) {
                                LocalTime ingresoReal = mejorEntrada.get();
                                if (ingresoReal.isAfter(ingEsperado.plusMinutes(10))) {
                                    anomalias.add(new AnomaliaDiariaDTO(dia, "Llegada tarde (" + nombreCargo + "): Fichó " + ingresoReal.format(timeFormatter) + "hs (Debía " + ingEsperado.format(timeFormatter) + "hs)"));
                                }
                            } else {
                                anomalias.add(new AnomaliaDiariaDTO(dia, "Falta fichada de ENTRADA (" + nombreCargo + ")"));
                            }

                            // --- EVALUACIÓN DE LA SALIDA DE ESTE CARGO ---
                            // Buscamos la salida con la misma ventana de lógica de 3 horas
                            Optional<LocalTime> mejorSalida = salidasHoy.stream()
                                    .map(f -> f.getHora().toLocalTime())
                                    .filter(hora -> Math.abs(ChronoUnit.MINUTES.between(hora, salEsperada)) <= 180)
                                    .min(Comparator.comparingLong(h -> Math.abs(ChronoUnit.MINUTES.between(h, salEsperada))));

                            if (mejorSalida.isPresent()) {
                                LocalTime salidaReal = mejorSalida.get();
                                if (salidaReal.isBefore(salEsperada)) {
                                    anomalias.add(new AnomaliaDiariaDTO(dia, "Salida anticipada (" + nombreCargo + "): Fichó " + salidaReal.format(timeFormatter) + "hs (Debía " + salEsperada.format(timeFormatter) + "hs)"));
                                }
                            } else {
                                anomalias.add(new AnomaliaDiariaDTO(dia, "Falta fichada de SALIDA (" + nombreCargo + ")"));
                            }
                        }
                    }
                }
                asistencias.add(new Asistencia(dia, estado));
            }
            informe.add(new AsistenciaPorAgenteDTO(agente, asistencias, anomalias));
        }
        return informe;
        /*
        LocalDate inicio = DateUtils.getPrimerDiaDelMes(mes);
        LocalDate fin = DateUtils.getUltimoDiaDelMes(mes);

        List<LocalDate> diasHabiles = DateUtils.getDiasLaborablesPorRango(inicio, fin);
        List<Agente> agentes = agenteRepository.findAll();
        List<AsistenciaPorAgenteDTO> informe = new ArrayList<>();

        for (Agente agente : agentes){

            List<Asistencia> asistencias = new ArrayList<>();
            for (LocalDate dia: diasHabiles){
                String estado = "";
                if(eventoService.esDiaLaboral(dia)){
                    LocalDateTime inicioDelDia = dia.atStartOfDay();
                    LocalDateTime finDelDia = dia.atTime(LocalTime.MAX);
                    List<Fichada> fichadas = fichadaRepository.findByAgenteAndHoraBetween(agente, inicioDelDia, finDelDia);

                    boolean entrada = fichadas.stream().anyMatch(f -> f.getTipoFichada().getNombre().toUpperCase().equals("ENTRADA"));
                    boolean salida = fichadas.stream().anyMatch(f -> f.getTipoFichada().getNombre().toUpperCase().equals("SALIDA"));

                    if (entrada && salida) {
                        estado = "P";
                    }else if (!fichadas.isEmpty()) {
                        if (!salida)
                            estado = "P<br/>N/S";
                    }else{
                        estado = "A";
                    }
                } else {
                    estado = "No se dictaron clases";
                }
                asistencias.add(new Asistencia(dia, estado));
            }
            informe.add(new AsistenciaPorAgenteDTO(agente, asistencias));
        }
        return informe;
        */
    }
}
