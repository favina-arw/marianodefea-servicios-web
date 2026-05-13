package com.marianodefea.servicios_web.model;

import com.marianodefea.servicios_web.utils.Horario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "agentes_cargos")
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

    @Builder.Default
    private boolean activo = true;

    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

    public static AgenteCargo crear(Agente agente, Cargo cargo, Horario horario, LocalDate fechaAlta) {
        return AgenteCargo.builder()
                .agente(agente)
                .cargo(cargo)
                .horario(horario)
                .activo(true)
                .fechaAlta(fechaAlta) // Usamos la que viene del formulario
                .build();
    }
}