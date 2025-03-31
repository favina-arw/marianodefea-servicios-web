package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.fichada.TipoFichada;
import com.marianodefea.servicios_web.repository.ITipoFichadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoFichadaService implements ITipoFichadaService{

    @Autowired
    private ITipoFichadaRepository tipoFichadaRepository;

    @Override
    public List<TipoFichada> findAll() {
        return tipoFichadaRepository.findAll();
    }

    @Override
    public Optional<TipoFichada> findById(Long id) {
        return tipoFichadaRepository.findById(id);
    }

    @Override
    public TipoFichada save(TipoFichada tipoFichada) {
        return tipoFichadaRepository.save(tipoFichada);
    }

    @Override
    public void deleteById(Long id) {
        tipoFichadaRepository.deleteById(id);
    }

    @Override
    public TipoFichada update(TipoFichada tipoFichada) {
        return tipoFichadaRepository.save(tipoFichada);
    }

    @Override
    public Optional<TipoFichada> findByIdentificador(char identificador) {
        return tipoFichadaRepository.findByIdentificador(identificador);
    }

    @Override
    public Optional<TipoFichada> findByNombre(String nombre) {
        return tipoFichadaRepository.findByNombre(nombre);
    }
}
