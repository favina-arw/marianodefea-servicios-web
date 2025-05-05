package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import com.marianodefea.servicios_web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class InformeService {
    @Autowired
    private IAgenteRepository agenteRepository;

    @Autowired
    private IFichadaRepository fichadaRepository;

    @Autowired
    private EventoService eventoService;

    public Map<Agente, Map<LocalDate, String>> generarInformeAsistenciaMensual(){
        LocalDate inicio = DateUtils.getPrimerDiaDelMesAnterior();
        LocalDate fin = DateUtils.getUltimoDiaDelMesAnterior();

        List<LocalDate> diasHabiles = DateUtils.getDiasLaborablesPorRango(inicio, fin);
        List<Agente> agentes = agenteRepository.findAll();
        Map<Agente, Map<LocalDate, String>> informe = new LinkedHashMap<>();

        for (Agente agente : agentes){
            Map<LocalDate, String> asistencias = new LinkedHashMap<>();

            for (LocalDate dia: diasHabiles){
                String diaFormateado = dia.toString();
                if(eventoService.esDiaLaboral(dia)){
                    LocalDateTime inicioDelDia = dia.atStartOfDay();
                    LocalDateTime finDelDia = dia.atTime(LocalTime.MAX);
                    List<Fichada> fichadas = fichadaRepository.findByAgenteAndHoraBetween(agente, inicioDelDia, finDelDia);

                    boolean entrada = fichadas.stream().anyMatch(f -> f.getTipoFichada().getNombre().toUpperCase().equals("ENTRADA"));
                    boolean salida = fichadas.stream().anyMatch(f -> f.getTipoFichada().getNombre().toUpperCase().equals("SALIDA"));

                    if (entrada && salida) {
                        asistencias.put(dia, "Presente");
                    }else if (!fichadas.isEmpty()) {
                        if (!salida)
                            asistencias.put(dia, "No registró su salida");
                    }else{
                        asistencias.put(dia, "Ausente");
                    }
                } else {
                    asistencias.put(dia, "No se dictaron clases");
                }
            }
            informe.put(agente, asistencias);
        }
        return informe;
    }
}
