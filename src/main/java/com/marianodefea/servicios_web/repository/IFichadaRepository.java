package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFichadaRepository extends JpaRepository<Fichada, Long> {
}
