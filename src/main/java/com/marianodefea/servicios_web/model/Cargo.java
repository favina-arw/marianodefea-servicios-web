package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.utils.Horario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private CargoTipo cargoTipo;

    // RELACIONES OPCIONALES:
    // Si es profe, se llenan ambas. Si es preceptor, solo curso. Si es director, ambas null.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id") // Permite nulos por defecto
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id") // Permite nulos por defecto
    private Curso curso;

    private boolean activo = true;

    @OneToMany(mappedBy = "cargo")
    private List<AgenteCargo> historialAsignaciones = new ArrayList<>();
}