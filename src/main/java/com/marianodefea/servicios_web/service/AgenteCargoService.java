package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.dto.AsignacionCargoDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.Cargo;
import com.marianodefea.servicios_web.model.AgenteCargo;
import com.marianodefea.servicios_web.repository.IAgenteCargoRepository;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import com.marianodefea.servicios_web.repository.ICargoRepository;
import com.marianodefea.servicios_web.utils.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgenteCargoService {

    @Autowired private IAgenteCargoRepository agenteCargoRepository;
    @Autowired private IAgenteRepository agenteRepository;
    @Autowired private ICargoRepository cargoRepository;

    @Transactional
    public void asignarCargo(AsignacionCargoDTO dto) {
        // Buscamos a las partes involucradas
        Agente agente = agenteRepository.findById(dto.getAgenteId())
                .orElseThrow(() -> new RuntimeException("El agente no existe."));

        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new RuntimeException("El cargo no existe."));

        // Armamos el embebible Horario usando tu Builder
        Horario horario = Horario.builder()
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .build();

        // Usamos la factory que armaste en el modelo para crear el contrato final
        AgenteCargo asignacion = AgenteCargo.crear(agente, cargo, horario, dto.getFechaAlta());

        agenteCargoRepository.save(asignacion);
    }
}