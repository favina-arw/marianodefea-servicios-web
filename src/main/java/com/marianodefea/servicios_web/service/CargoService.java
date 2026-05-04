package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Cargo;
import com.marianodefea.servicios_web.repository.ICargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

    @Autowired
    private ICargoRepository cargoRepository;

    @Transactional(readOnly = true)
    public List<Cargo> obtenerTodos() {
        return cargoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cargo> obtenerActivos() {
        return cargoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Cargo> buscarPorId(Long id) {
        return cargoRepository.findById(id);
    }

    @Transactional
    public Cargo guardar(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        cargoRepository.findById(id).ifPresent(cargo -> {
            cargo.setActivo(estado);
            cargoRepository.save(cargo);
        });
    }
}