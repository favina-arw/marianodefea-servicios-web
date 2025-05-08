package com.marianodefea.servicios_web.model.fichada;

import com.marianodefea.servicios_web.model.Agente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fichadas")
public class Fichada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "tipo_fichada_id", nullable = false)
    private TipoFichada tipoFichada;
    private LocalDateTime hora;
    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;
    @Column(name = "tipo_registro", nullable = false)
    private String tipoRegistro = "MANUAL";

}
