package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
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
    @Column(unique = true, nullable = false)
    private String dni;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @OneToMany()
    private Cargo cargo;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    private Boolean activo;
    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("hora DESC")
    private List<Fichada> fichadas = new ArrayList<>();

}
