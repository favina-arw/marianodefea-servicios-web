package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.AgenteDTO;
import com.marianodefea.servicios_web.dto.CargoAsignadoDTO;
import com.marianodefea.servicios_web.dto.ListarAgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agentes")
public class AgenteController {

    @Autowired
    AgenteService agenteService;

    @GetMapping("/")
    public String listarAgentes(Model model){

        List<ListarAgenteDTO> agentes = agenteService.findAll().stream().map(agente ->{
            List<CargoAsignadoDTO> cargosAsignados = agente.getCargosAsignados().stream()
                    .map(ca -> new CargoAsignadoDTO(
                            ca.getId(),
                            ca.getCargo().getNombre(),
                            ca.getHorario() != null ? ca.getHorario().getHoraInicio().toString() : "-",
                            ca.getHorario() != null ? ca.getHorario().getHoraFin().toString() : "-",
                            ca.isActivo()
                    )).collect(Collectors.toList());

            return new ListarAgenteDTO(
                    agente.getId(),
                    agente.getCuil(),
                    agente.getDni(),
                    agente.getNombre(),
                    agente.getApellido(),
                    agente.isActivo(),
                    cargosAsignados
            );
        }).collect(Collectors.toList());
        model.addAttribute("agentes", agentes);
        return "user/listar_agentes";
    }

    @GetMapping("/crearAgente")
    public String crearAgenteFormulario(){
        return "user/crear_agente";
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
        nuevoAgente.setNombre(agenteDTO.getNombre().toUpperCase());
        nuevoAgente.setApellido(agenteDTO.getApellido().toUpperCase());

        Agente agenteCreado = agenteService.save(nuevoAgente);
        if (agenteCreado == null) {
            model.addAttribute("error", "Hubo un problema, el agente no fue registrado");
            return "agentes/crear_agente";
        }
        model.addAttribute("success", "Agente: " + agenteCreado.getNombre() + ", " + agenteCreado.getApellido() + ". Creado con éxito.");
        return "redirect:/agentes/";
    }


}
