package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Materia;
import com.marianodefea.servicios_web.repository.IMateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {

    @Autowired
    private IMateriaRepository materiaRepository;

    @Transactional(readOnly = true)
    public List<Materia> obtenerTodas() {
        return materiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Materia> obtenerActivas() {
        return materiaRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Materia> buscarPorId(Long id) {
        return materiaRepository.findById(id);
    }

    @Transactional
    public Materia guardar(Materia materia) {
        // Aseguramos que el nombre esté siempre en mayúsculas para evitar duplicados como "Matematica" y "MATEMATICA"
        if (materia.getNombre() != null) {
            materia.setNombre(materia.getNombre().toUpperCase().trim());
        }
        return materiaRepository.save(materia);
    }

    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        materiaRepository.findById(id).ifPresent(materia -> {
            materia.setActivo(estado);
            materiaRepository.save(materia);
        });
    }
}