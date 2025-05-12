package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.utils.Horario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgenteCargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @Embedded
    private Horario horario;
    private boolean activo;

    public static AgenteCargo crear(Agente agente, Cargo cargo, Horario horario) {
        return AgenteCargo.builder()
                .agente(agente)
                .cargo(cargo)
                .horario(horario)
                .activo(true)
                .build();
    }
}