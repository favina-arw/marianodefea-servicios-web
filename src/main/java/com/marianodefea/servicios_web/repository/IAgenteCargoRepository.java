package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.AgenteCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAgenteCargoRepository extends JpaRepository<AgenteCargo, Long> {
    // Forzamos a que traiga el cargo y el tipo de cargo en el mismo viaje a la BD
    @Query("SELECT ac FROM AgenteCargo ac JOIN FETCH ac.cargo c JOIN FETCH c.cargoTipo WHERE ac.id = :id")
    Optional<AgenteCargo> findByIdConRelaciones(@Param("id") Long id);

    @Query("SELECT ac FROM AgenteCargo ac JOIN FETCH ac.cargo c JOIN FETCH c.cargoTipo ct WHERE ac.id = :id")
    Optional<AgenteCargo> findByIdCompleto(@Param("id") Long id);

    // Traer todo los cargos activos de un agente en particulare
    List<AgenteCargo> findByAgente_IdAndActivoTrue(Long id);
}