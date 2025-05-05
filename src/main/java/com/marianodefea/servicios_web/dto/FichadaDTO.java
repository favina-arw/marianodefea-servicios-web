package com.marianodefea.servicios_web.dto;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FichadaDTO {
    private String tipoFichada;
    private String hora;
    private String agenteNombre;

    public FichadaDTO(Fichada f) {
        this.tipoFichada = f.getTipoFichada().getNombre(); // o getTipo().name()
        this.hora = f.getHora().toString();
        this.agenteNombre = f.getAgente().getNombre();
    }
}
