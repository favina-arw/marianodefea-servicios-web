package com.marianodefea.servicios_web.dto;

import com.marianodefea.servicios_web.model.Cargo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListarAgenteDTO {

    private String cuil;
    private String dni;
    private String nombre;
    private String apellido;
    private Cargo cargo;
    private boolean activo;

    public boolean isActivo(){
    return activo;
    }

}
