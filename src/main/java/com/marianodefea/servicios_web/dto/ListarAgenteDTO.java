package com.marianodefea.servicios_web.dto;

import com.marianodefea.servicios_web.model.Cargo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListarAgenteDTO {

    private Long id;
    private String cuil;
    private String dni;
    private String nombre;
    private String apellido;
    private boolean activo;
    private List<CargoAsignadoDTO> cargosAsignados;

    public String isActivo(){
    return this.activo ? "Activo" : "Inactivo";
    }

}
