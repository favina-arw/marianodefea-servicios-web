package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FichadaService implements IFichadaService{
    @Autowired
    private IFichadaRepository fichadaRepository;

    @Override
    public List<Fichada> findAll() {
        return fichadaRepository.findAll();
    }

    @Override
    public List<Fichada> findTop10ByOrderByHoraDesc() {
        return fichadaRepository.findTop10ByOrderByHoraDesc();
    }

    @Override
    public Optional<Fichada> findById(Long id) {
        return fichadaRepository.findById(id);
    }

    @Override
    public Fichada save(Fichada fichada) {
        return fichadaRepository.save(fichada);
    }

    @Override
    public void deleteById(Long id) {
        fichadaRepository.deleteById(id);
    }

    @Override
    public Fichada update(Fichada fichada) {
        return fichadaRepository.save(fichada);
    }

    @Override
    public Page<Fichada> buscarFichadas(String nombre, String apellido, String tipoFichada, LocalDate desde, LocalDate hasta, String dni, Pageable pageable) {
        return fichadaRepository.buscarPorFiltros(nombre, apellido, tipoFichada, desde, hasta, dni, pageable);
    }

}
