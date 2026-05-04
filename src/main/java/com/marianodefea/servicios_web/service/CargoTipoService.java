package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.CargoTipo;
import com.marianodefea.servicios_web.repository.ICargoTipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CargoTipoService {

    @Autowired
    private ICargoTipoRepository cargoTipoRepository;

    @Transactional(readOnly = true)
    public List<CargoTipo> obtenerTodos() {
        return cargoTipoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CargoTipo> obtenerActivos() {
        return cargoTipoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<CargoTipo> buscarPorId(Long id) {
        return cargoTipoRepository.findById(id);
    }

    @Transactional
    public CargoTipo guardar(CargoTipo cargoTipo) {
        if (cargoTipo.getNombre() != null) {
            cargoTipo.setNombre(cargoTipo.getNombre().toUpperCase().trim());
        }
        return cargoTipoRepository.save(cargoTipo);
    }

    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        cargoTipoRepository.findById(id).ifPresent(tipo -> {
            tipo.setActivo(estado);
            cargoTipoRepository.save(tipo);
        });
    }
}