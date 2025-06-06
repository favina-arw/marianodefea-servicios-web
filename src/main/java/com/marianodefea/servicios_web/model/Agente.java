package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.model.fichada.Fichada;
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
    private boolean activo = true;

    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AgenteCargo> cargosAsignados = new ArrayList<>();

    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("hora DESC")
    private List<Fichada> fichadas = new ArrayList<>();

    public void asignarCargo(Cargo cargo, Horario horario) {
        AgenteCargo agenteCargo = AgenteCargo.crear(this, cargo, horario);
        cargosAsignados.add(agenteCargo);
    }

    public Boolean isActivo(){
        return this.activo;
    }

}
