package com.marianodefea.servicios_web.repository;

import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.specification.FichadaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IFichadaRepository extends JpaRepository<Fichada, Long>, JpaSpecificationExecutor<Fichada> {
    default Page<Fichada> buscarPorFiltros(String nombre, String apellido, String tipoFichada, LocalDate fechaDesde, LocalDate fechaHasta, String dni, Pageable pageable) {
        Specification<Fichada> specification = FichadaSpecification.buscarPorFiltros(nombre, apellido, tipoFichada, fechaDesde, fechaHasta, dni);
        return findAll(specification, pageable);
    }
    List<Fichada> findByAgenteAndHoraBetween(Agente agente, LocalDateTime start, LocalDateTime end);
    List<Fichada> findTop10ByOrderByHoraDesc();

    @Query("SELECT f FROM Fichada f " +
            "JOIN f.tipoFichada tf " +
            "WHERE tf.identificador = 'E' " +
            "AND CAST(f.hora AS localdate) = :hoy " +
            "AND f.hora = (" +
            "SELECT MAX(f2.hora) FROM Fichada f2 " +
            "WHERE f2.agente = f.agente " +
            "AND CAST(f2.hora AS localdate) = CAST(f.hora AS localdate)" +
            ")")
    List<Fichada> findUltimasEntradasSinSalidaHoy(@Param("hoy") LocalDate hoy);
}
