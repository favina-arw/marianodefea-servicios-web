package com.marianodefea.servicios_web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargoAsignadoDTO {
    private Long idAgenteCargo;
    private String nombreCargo;
    private String horaEntrada;
    private String horaSalida;
    private boolean activo;
}
