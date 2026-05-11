package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.AgenteDTO;
import com.marianodefea.servicios_web.dto.CargoAsignadoDTO;
import com.marianodefea.servicios_web.dto.ListarAgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agentes")
public class AgenteController {

    @Autowired
    AgenteService agenteService;

    @GetMapping("/")
    public String listarAgentes(Model model){
        List<ListarAgenteDTO> agentes = agenteService.obtenerTodosLosAgentesDTO();
        model.addAttribute("agentes", agentes);
        return "user/listar_agentes";
    }

    @GetMapping("/crearAgente")
    public String crearAgenteFormulario(Model model){
        model.addAttribute("agenteDTO", new AgenteDTO());
        return "user/crear_agente";
    }

    @PostMapping("/crearAgente")
    public String crearAgente(@ModelAttribute AgenteDTO agenteDTO, Model model, RedirectAttributes redirectAttributes){

        Agente nuevoAgente = new Agente();
        nuevoAgente.setDni(agenteDTO.getDni());
        if (agenteDTO.getCuil() == null || agenteDTO.getCuil().isBlank()){
            nuevoAgente.setCuil(null);
        }else{
            nuevoAgente.setCuil(agenteDTO.getCuil());
        }
        nuevoAgente.setNombre(agenteDTO.getNombre().toUpperCase());
        nuevoAgente.setApellido(agenteDTO.getApellido().toUpperCase());

        try {
            Agente agenteCreado = agenteService.save(nuevoAgente);
            redirectAttributes.addFlashAttribute("success", "Agente: "+ agenteCreado.getApellido() + ", " + agenteCreado.getNombre() + " creado con éxito.");
            return "redirect:/agentes/";
        }catch (DataIntegrityViolationException e){
            model.addAttribute("error", "Ya existe un agente registrado con ese DNI o CUIL.");
            return "user/crear_agente";
        }
    }

    @GetMapping("/baja/{id}")
    public String desactivarAgente (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agenteService.deactivarAgente(id);
            redirectAttributes.addFlashAttribute("success", "El agente fue desactivado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al intentar dar de baja al agente");
        }
        return "redirect:/agentes/";
    }

    @GetMapping("/alta/{id}")
    public String activarAgente (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agenteService.activarAgente(id);
            redirectAttributes.addFlashAttribute("success", "El agente fue activado con éxito");
        } catch ( Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al intentar activar al agente");
        }
        return "redirect:/agentes/";
    }

    @Transactional(readOnly = true)
    @GetMapping("/ver/{id}")
    public String verAgente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Agente> agenteOptional = agenteService.findByIdConCargosCompletos(id);
        if(agenteOptional.isPresent()) {
            model.addAttribute("agente", agenteOptional.get());
            return "user/ver_agente";
        } else {
            redirectAttributes.addFlashAttribute("error", "Agente no encontrado");
            return "redirect:/agentes/";
        }
    }

    @GetMapping("/editar/{id}")
    public String actualizarAgente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Agente> agenteOptional = agenteService.findById(id);

        if (agenteOptional.isPresent()){
            model.addAttribute("agente", agenteOptional.get());
            return "user/editar_agente";
        }else{
            redirectAttributes.addFlashAttribute("error", "Agente no encontrado");
            return "redirect:/agentes/";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarAgente(@ModelAttribute Agente agente, RedirectAttributes redirectAttributes) {
        try {
            agenteService.save(agente);
            redirectAttributes.addFlashAttribute("success", "El perfil de " + agente.getApellido() + " se actualizó correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al intentar guardar los cambios.");
        }
        return "redirect:/agentes/ver/" + agente.getId();
    }

}
