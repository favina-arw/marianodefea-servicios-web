package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.AgenteCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAgenteCargoRepository extends JpaRepository<AgenteCargo, Long> {
}