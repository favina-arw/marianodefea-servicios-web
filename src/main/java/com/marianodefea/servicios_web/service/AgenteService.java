package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.dto.CargoAsignadoDTO;
import com.marianodefea.servicios_web.dto.ListarAgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgenteService implements IAgenteService{
    @Autowired
    private IAgenteRepository agenteRepository;

    @Override
    public List<Agente> findAll() {
        return agenteRepository.findAll();
    }

    @Override
    public Optional<Agente> findById(Long id) {
        return agenteRepository.findById(id);
    }

    @Override
    public Optional<Agente> findByDni(String dni) {
        return agenteRepository.findByDni(dni);
    }

    @Override
    public Optional<Agente> findByCuil(String cuil) {
        return agenteRepository.findByCuil(cuil);
    }

    @Override
    public Agente save(Agente agente) {
        return agenteRepository.save(agente);
    }

    @Override
    public void deleteById(Long id) {
        agenteRepository.deleteById(id);
    }

    @Override
    public Agente update(Agente agente) {
        return agenteRepository.save(agente);
    }

    @Transactional
    public void deactivarAgente(Long id){
        Agente agente = agenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado"));

        agente.setActivo(false);
        agenteRepository.save(agente);
    }

    @Transactional
    public void activarAgente(Long id) {
        Agente agente = agenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado"));

        agente.setActivo(true);
        agenteRepository.save(agente);
    }

    @Transactional(readOnly = true)
    public List<ListarAgenteDTO> obtenerTodosLosAgentesDTO(){
        return agenteRepository.findAll().stream().map(agente -> {
            List<CargoAsignadoDTO> cargosAsignados = agente.getCargosAsignados().stream()
                    .map(ca -> new CargoAsignadoDTO(
                    ca.getId(),
                    ca.getCargo().getCargoTipo().getNombre(),
                    ca.getHorario() != null ? ca.getHorario().getHoraInicio().toString() : "-",
                    ca.getHorario() != null ? ca.getHorario().getHoraFin().toString() : "-",
                    ca.isActivo()
            )).collect(Collectors.toList());

            return new ListarAgenteDTO(
                    agente.getId(),
                    agente.getCuil(),
                    agente.getDni(),
                    agente.getNombre(),
                    agente.getApellido(),
                    agente.isActivo(),
                    cargosAsignados
            );
        }).collect(Collectors.toList());
    }

    public Optional<Agente> findByIdConCargosCompletos(Long id) {
        return agenteRepository.findByIdConCargosCompletos(id);
    }
}
