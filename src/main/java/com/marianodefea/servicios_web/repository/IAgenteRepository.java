package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByDni(String dni);
    Optional<Agente> findByCuil(String cuil);

    @Query("SELECT a FROM Agente a LEFT JOIN FETCH a.cargosAsignados ac LEFT JOIN FETCH ac.cargo c LEFT JOIN FETCH c.cargoTipo WHERE a.id = :id")
    Optional<Agente> findByIdConCargosCompletos(@Param("id") Long id);
}
