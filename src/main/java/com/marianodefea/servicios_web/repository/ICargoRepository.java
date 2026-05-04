package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICargoRepository extends JpaRepository<Cargo, Long> {
    List<Cargo> findByActivoTrue();
}