package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.utils.enums.Ciclo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Matemática", "Prácticas del Lenguaje"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ciclo ciclo;

    private boolean activo = true;
}
