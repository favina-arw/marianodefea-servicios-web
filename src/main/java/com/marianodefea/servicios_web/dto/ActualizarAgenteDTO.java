package com.marianodefea.servicios_web.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarAgenteDTO {
    private Long id;
    private String cuil;
    private String dni;
    private String nombre;
    private String apellido;
    private String numeroLegajo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String domicilio;
    private String telefono;
    private String titulo;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;
    private boolean activo;
}
