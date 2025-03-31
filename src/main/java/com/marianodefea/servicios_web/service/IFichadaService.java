package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.fichada.Fichada;

import java.util.List;
import java.util.Optional;

public interface IFichadaService {
    List<Fichada> findAll();
    Optional<Fichada> findById(Long id);
    Fichada save(Fichada fichada);
    void deleteById(Long id);
    Fichada update(Fichada fichada);
}
