package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Curso;
import com.marianodefea.servicios_web.repository.ICursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private ICursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Curso> obtenerActivos() {
        return cursoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        cursoRepository.findById(id).ifPresent(curso -> {
            curso.setActivo(estado);
            cursoRepository.save(curso);
        });
    }
}