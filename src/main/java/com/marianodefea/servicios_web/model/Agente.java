package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Agente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cuil;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    private String cargo;
    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Fichada> fichadas = new ArrayList<>();

}
