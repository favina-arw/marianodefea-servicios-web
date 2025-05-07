package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.dto.AsistenciaPorAgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import com.marianodefea.servicios_web.utils.Asistencia;
import com.marianodefea.servicios_web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InformeService {
    @Autowired
    private IAgenteRepository agenteRepository;

    @Autowired
    private IFichadaRepository fichadaRepository;

    @Autowired
    private EventoService eventoService;

    public List<AsistenciaPorAgenteDTO> generarInformeAsistenciaMensual(){
        LocalDate inicio = DateUtils.getPrimerDiaDelMesAnterior();
        LocalDate fin = DateUtils.getUltimoDiaDelMesAnterior();

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
                        estado = "Presente";
                    }else if (!fichadas.isEmpty()) {
                        if (!salida)
                            estado = (!salida) ? "No registró su salida" : "Incompleto";
                    }else{
                        estado = "Ausente";
                    }
                } else {
                    estado = "No se dictaron clases";
                }
                asistencias.add(new Asistencia(dia, estado));
            }
            informe.add(new AsistenciaPorAgenteDTO(agente, asistencias));
        }
        return informe;
    }
}
