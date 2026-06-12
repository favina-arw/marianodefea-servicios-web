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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AgenteCargoService {

    @Autowired
    private IAgenteCargoRepository agenteCargoRepository;
    @Autowired
    private IAgenteRepository agenteRepository;
    @Autowired
    private ICargoRepository cargoRepository;

    public Optional<AgenteCargo> findById(Long id) {
        return agenteCargoRepository.findById(id);
    }

    public Optional<AgenteCargo> findByIdConRelaciones(Long id) {
        return agenteCargoRepository.findByIdConRelaciones(id);
    }

    public AgenteCargo save(AgenteCargo agenteCargo) {
        return agenteCargoRepository.save(agenteCargo);
    }

    public Optional<AgenteCargo> findByIdCompleto(Long id) {
        return agenteCargoRepository.findByIdCompleto(id);
    }

    @Transactional(readOnly = true)
    public List<AgenteCargo> findAll() {
        return agenteCargoRepository.findAll();
    }

    @Transactional
    public void asignarCargo(AsignacionCargoDTO dto) {
        Horario nuevoHorario = new Horario(dto.getHoraInicio(), dto.getHoraFin());

        if (existeSuperposicion(dto.getAgenteId(), nuevoHorario, null))
            throw new IllegalArgumentException("El horario ingresado se superpone con otro cargo activo de éste agente.");

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

    public boolean existeSuperposicion(Long agenteId, Horario nuevoHorario, Long idCargoAExcluir) {
        List<AgenteCargo> cargosActivos = agenteCargoRepository.findByAgente_IdAndActivoTrue(agenteId);

        for (AgenteCargo cargoExistente : cargosActivos) {
            if (idCargoAExcluir != null && cargoExistente.getId().equals(idCargoAExcluir)) {
                continue;
            }

            if (cargoExistente.getHorario() == null || cargoExistente.getHorario().getHoraInicio() == null) {
                continue;
            }

            if (cargoExistente.getHorario().superponeCon(nuevoHorario)) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public void registrarBaja(Long idAsignacion, LocalDate fechaBaja) {
        AgenteCargo asignacion = agenteCargoRepository.findById(idAsignacion)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la asignación solicitada"));
        asignacion.setActivo(false);
        asignacion.setFechaBaja(fechaBaja);
        agenteCargoRepository.save(asignacion);
    }

}