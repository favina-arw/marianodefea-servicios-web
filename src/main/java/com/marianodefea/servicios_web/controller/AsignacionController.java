package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.AsignacionCargoDTO;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import com.marianodefea.servicios_web.repository.ICargoRepository;
import com.marianodefea.servicios_web.service.AgenteCargoService;
import com.marianodefea.servicios_web.service.AgenteService;
import com.marianodefea.servicios_web.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/asignaciones")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AsignacionController {

    @Autowired private AgenteCargoService agenteCargoService;
    @Autowired private AgenteService agenteService;
    @Autowired private CargoService cargoService;

    @GetMapping("/crear")
    public String formAsignar(Model model) {
        // Le pasamos el DTO vacío a Thymeleaf para que mapee el formulario
        model.addAttribute("asignacionDTO", new AsignacionCargoDTO());

        // Llenamos los desplegables usando los Services, respetando la arquitectura
        model.addAttribute("agentes", agenteService.obtenerTodosLosAgentesDTO());
        model.addAttribute("cargos", cargoService.obtenerActivos());
        return "asignaciones/crear_asignacion";
    }

    @PostMapping("/guardar")
    public String guardarAsignacion(@ModelAttribute("asignacionDTO") AsignacionCargoDTO dto, RedirectAttributes redirectAttributes) {
        try {
            // Le pasamos todo el paquete (IDs, fechas, horas) al Service de una
            agenteCargoService.asignarCargo(dto);
            redirectAttributes.addFlashAttribute("success", "¡Cargo asignado al agente con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la asignación: " + e.getMessage());
        }
        // Lo mandamos a la tabla principal de agentes
        return "redirect:/agentes/";
    }
}