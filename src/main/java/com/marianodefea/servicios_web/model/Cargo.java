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
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cargos")
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private CargoTipo cargoTipo;
    @Embedded
    private Horario horario;
    private boolean activo;

    @ManyToOne
    private Agente agente;
}
