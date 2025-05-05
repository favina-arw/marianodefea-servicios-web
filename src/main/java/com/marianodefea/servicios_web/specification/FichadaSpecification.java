package com.marianodefea.servicios_web.specification;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import jakarta.persistence.criteria.Predicate;

public class FichadaSpecification {

    public static Specification<Fichada> buscarPorFiltros(String nombre, String apellido, String tipoFichada, LocalDate fechaDesde, LocalDate fechaHasta, String dni) {

        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (tipoFichada != null && !tipoFichada.isEmpty()) {
                predicate = builder.and(predicate, builder.equal(builder.lower(root.get("tipoFichada").get("nombre")), tipoFichada.toLowerCase()));
            }

            if (dni != null && !dni.isEmpty()) {
                predicate = builder.and(predicate, builder.equal(root.get("agente").get("dni"), dni));
            }

            if (nombre != null && !nombre.isEmpty()) {
                predicate = builder.and(predicate, builder.like(
                        builder.lower(root.get("agente").get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }

            if (apellido != null && !apellido.isEmpty()) {
                predicate = builder.and(predicate, builder.like(
                        builder.lower(root.get("agente").get("apellido")), "%" + apellido.toLowerCase() + "%"));
            }

            if (fechaDesde != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("hora"), fechaDesde.atStartOfDay()));
            }

            if (fechaHasta != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("hora"), fechaHasta.atTime(23, 59, 59)));
            }

            return predicate;
        };
    }
}