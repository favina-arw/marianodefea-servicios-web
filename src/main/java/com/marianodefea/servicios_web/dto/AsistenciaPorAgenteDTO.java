package com.marianodefea.servicios_web.dto;

import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.utils.Asistencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaPorAgenteDTO {
    private Agente agente;
    private List<Asistencia> asistencias;

    public Optional<Asistencia> getAsistenciaPorDia(LocalDate dia) {
        if (asistencias == null) {
            return Optional.empty();
        }
        return asistencias.stream()
                .filter(a -> a != null && a.getDia() != null && a.getDia().equals(dia))
                .findFirst();
    }
}