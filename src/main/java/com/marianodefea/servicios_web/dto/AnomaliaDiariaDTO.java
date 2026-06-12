package com.marianodefea.servicios_web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
@AllArgsConstructor
public class AnomaliaDiariaDTO {
    private LocalDate fecha;
    private String observacion;
}
