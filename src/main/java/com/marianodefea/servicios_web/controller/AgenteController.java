package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.AgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agentes")
public class AgenteController {

    @Autowired
    AgenteService agenteService;

    @GetMapping("/crearAgente")
    public String crearAgenteFormulario(){
        return "crear_agente";
    }

    @PostMapping("/crearAgente")
    public String crearAgente(@ModelAttribute AgenteDTO agenteDTO, Model model){
        System.out.println("Entro a crear agente");
        System.out.println(agenteDTO);
        Agente nuevoAgente = new Agente();
        nuevoAgente.setDni(agenteDTO.getDni());
        if (agenteDTO.getCuil().isBlank() || agenteDTO.getCuil().isEmpty()){
            nuevoAgente.setCuil(null);
        }else{
            nuevoAgente.setCuil(agenteDTO.getCuil());
        }
        nuevoAgente.setNombre(agenteDTO.getNombre());
        nuevoAgente.setApellido(agenteDTO.getApellido());

        Agente agenteCreado = agenteService.save(nuevoAgente);
        if (agenteCreado == null) {
            model.addAttribute("error", "Hubo un problema, el agente no fur registrado");
            return "agentes/crear_agente";
        }
        model.addAttribute("success", "Agente: " + agenteCreado.getNombre() + ", " + agenteCreado.getApellido() + ". Creado con éxito.");
        return "public/fichaje";
    }
}
