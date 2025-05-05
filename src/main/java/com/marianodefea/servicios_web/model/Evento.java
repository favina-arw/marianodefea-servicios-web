package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.utils.enums.TipoEvento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private TipoEvento tipo;
    private String descripcion;
}

