package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFichadaService {
    List<Fichada> findAll();
    List<Fichada> findTop10ByOrderByHoraDesc();
    Page<Fichada> buscarFichadas(String nombre, String apellido, String tipoFichada, LocalDate desde, LocalDate hasta, String dni, Pageable pageable);
    Optional<Fichada> findById(Long id);
    Fichada save(Fichada fichada);
    void deleteById(Long id);
    Fichada update(Fichada fichada);
}
