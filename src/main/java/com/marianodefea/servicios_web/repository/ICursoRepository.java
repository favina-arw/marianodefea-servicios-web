package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByActivoTrue(); // Trae solo los cursos activos
}