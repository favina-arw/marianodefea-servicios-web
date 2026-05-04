package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.utils.enums.Ciclo;
import com.marianodefea.servicios_web.utils.enums.Turno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anio; // Ej: 1, 2, 3, 4, 5, 6

    @Column(nullable = false)
    private Integer division; // Ej: 1, 2, 3, 4

    // Cambiamos el String por el Enum de Turno
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    // Agregamos el Ciclo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ciclo ciclo;

    private boolean activo = true;

    // Método de ayuda para que sea fácil imprimirlo en las vistas (Ej: "3° 2da - Mañana")
    public String getNombreCompleto() {
        return anio + "° " + division + "da" + (turno != null ? " - " + turno : "");
    }
}