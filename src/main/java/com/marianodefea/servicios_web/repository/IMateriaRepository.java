package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByActivoTrue(); // Trae solo las materias activas
}
