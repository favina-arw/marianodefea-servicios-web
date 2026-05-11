package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.utils.Horario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agentes")
public class Agente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cuil;

    @Column(nullable = false)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    private Long numeroLegajo;

    private LocalDate fechaNacimiento;

    private String domicilio;

    private String telefono;

    private String titulo;

    private String email;

    private LocalDate fechaIngreso;

    private boolean activo = true;


    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgenteCargo> cargosAsignados = new ArrayList<>();

    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("hora DESC")
    private List<Fichada> fichadas = new ArrayList<>();

}
