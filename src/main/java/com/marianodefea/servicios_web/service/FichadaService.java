package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
