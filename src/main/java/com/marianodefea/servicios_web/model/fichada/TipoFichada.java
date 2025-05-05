package com.marianodefea.servicios_web.model.fichada;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoFichada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Character identificador;
    @Column(unique = true, nullable = false)
    private String nombre;

    @Override
    public String toString(){
        return this.nombre;
    }
}
