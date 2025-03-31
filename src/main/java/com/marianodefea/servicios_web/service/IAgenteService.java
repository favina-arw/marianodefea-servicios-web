package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Agente;

import java.util.List;
import java.util.Optional;

public interface IAgenteService {
    List<Agente> findAll();
    Optional<Agente> findById(Long id);
    Optional<Agente> findByDni(String dni);
    Optional<Agente> findByCuil(String cuil);
    Agente save(Agente agente);
    void deleteById(Long id);
    Agente update(Agente agente);
}
