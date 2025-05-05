package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Evento;
import com.marianodefea.servicios_web.utils.enums.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IEventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByFecha(LocalDate fecha);
    boolean existsByFechaAndTipo(LocalDate fecha, TipoEvento tipo);
}
