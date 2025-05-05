package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.Evento;
import com.marianodefea.servicios_web.repository.IEventoRepository;
import com.marianodefea.servicios_web.utils.enums.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService implements IEventoService{

    @Autowired
    private IEventoRepository eventoRepository;

    @Override
    public boolean esDiaLaboral(LocalDate dia) {
        DayOfWeek dow = dia.getDayOfWeek();
        boolean esFinDeSemana = dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;

        List<Evento> eventos = eventoRepository.findByFecha(dia);
        boolean esFeriadoNacional = eventos.stream().anyMatch(event -> event.getTipo() == TipoEvento.FERIADO_NACIONAL);
        boolean esFeriadoProvincial = eventos.stream().anyMatch(event -> event.getTipo() == TipoEvento.FERIADO_PROVINCIAL);
        boolean esCierreCompleto = eventos.stream().anyMatch(evento -> evento.getTipo() == TipoEvento.CIERRE_COMPLETO);


        return !esFinDeSemana && !esFeriadoNacional && !esFeriadoProvincial && !esCierreCompleto;
    }
    /*
    @Override
    public boolean esTurnoLaborable(LocalDate dia, Turno turno) {
        List<Evento> eventos = eventoRepository.findByFecha(dia);

        if (turno.getNombre().toUpperCase.equals("MANIANA")) {
            boolean esCierreTurnoManiana = eventos.stream().anyMatch(evento -> evento.getTipo() == TipoEvento.CIERRE_TURNO_MANIANA);
            return !esCierreTurnoManiana;
        }

        if (turno.getNombre().toUpperCase.equals("TARDE")) {
            boolean esCierreTurnoTarde = eventos.stream().anyMatch(evento -> evento.getTipo() == TipoEvento.CIERRE_TURNO_TARDE);
            return !esCierreTurnoTarde;
        }

    }
    */
}
