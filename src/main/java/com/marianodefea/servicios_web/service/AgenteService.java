package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
