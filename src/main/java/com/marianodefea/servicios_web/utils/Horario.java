package com.marianodefea.servicios_web.utils;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Horario {
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Método útil para validaciones
    public boolean solapaCon(Horario otro) {
        return this.horaInicio.isBefore(otro.horaFin) &&
                this.horaFin.isAfter(otro.horaInicio);
    }
}
