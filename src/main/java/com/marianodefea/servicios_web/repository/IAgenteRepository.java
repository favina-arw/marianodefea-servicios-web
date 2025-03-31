package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByDni(String dni);
    Optional<Agente> findByCuil(String cuil);
}
