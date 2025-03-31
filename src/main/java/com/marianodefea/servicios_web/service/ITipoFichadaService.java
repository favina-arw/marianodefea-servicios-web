package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.fichada.TipoFichada;

import java.util.List;
import java.util.Optional;

public interface ITipoFichadaService {
    List<TipoFichada> findAll();
    Optional<TipoFichada> findById(Long id);
    TipoFichada save(TipoFichada tipoFichada);
    void deleteById(Long id);
    TipoFichada update(TipoFichada tipoFichada);
    Optional<TipoFichada> findByIdentificador(char identificador);
    Optional<TipoFichada> findByNombre(String nombre);
}
