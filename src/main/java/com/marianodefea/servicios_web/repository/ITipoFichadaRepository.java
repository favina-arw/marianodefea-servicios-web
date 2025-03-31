package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.fichada.TipoFichada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITipoFichadaRepository extends JpaRepository<TipoFichada, Long> {
    Optional<TipoFichada> findByIdentificador(char identificador);
    Optional<TipoFichada> findByNombre(String nombre);
}
