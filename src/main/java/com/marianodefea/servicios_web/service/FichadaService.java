package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import com.marianodefea.servicios_web.service.interfaces.IFichadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FichadaService implements IFichadaService {
    @Autowired
    private final IFichadaRepository fichadaRepository;
    private final AgenteService agenteService;
    private final TipoFichadaService tipoFichadaService;

    public FichadaService(IFichadaRepository fichadaRepository,
                          AgenteService agenteService,
                          TipoFichadaService tipoFichadaService) {
        this.fichadaRepository = fichadaRepository;
        this.agenteService = agenteService;
        this.tipoFichadaService = tipoFichadaService;
    }

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

    @Override
    public Fichada registrarFichada(String dni){
        Optional<Agente> agenteOpt = (dni.length() <= 8) ? agenteService.findByDni(dni) : agenteService.findByCuil(dni);

        if (agenteOpt.isEmpty()){
            throw new IllegalArgumentException("No se encuentra Agente con DNI/CUIL: " + dni);
        }

        Agente agente = agenteOpt.get();

        Optional<Fichada> ultimafichada = fichadaRepository.findFirstByAgenteOrderByHoraDesc(agente);

        char tipoIdentificador = 'E';
        if (ultimafichada.isPresent() && ultimafichada.get().getTipoFichada().getIdentificador().equals('E')){
            tipoIdentificador = 'S';
        }

        Fichada nuevaFichada = new Fichada();
        nuevaFichada.setAgente(agente);
        nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoIdentificador).get());
        nuevaFichada.setHora(LocalDateTime.now());

        return fichadaRepository.save(nuevaFichada);
    }

}
