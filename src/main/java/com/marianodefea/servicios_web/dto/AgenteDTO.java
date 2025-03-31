package com.marianodefea.servicios_web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgenteDTO {
    private String dni;
    private String cuil;
    private String nombre;
    private String apellido;
}
