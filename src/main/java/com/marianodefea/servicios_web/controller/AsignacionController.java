package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.ActualizarAgenteCargoDTO;
import com.marianodefea.servicios_web.dto.AsignacionCargoDTO;
import com.marianodefea.servicios_web.dto.mapper.AgenteCargoMapper;
import com.marianodefea.servicios_web.model.AgenteCargo;
import com.marianodefea.servicios_web.repository.IAgenteRepository;
import com.marianodefea.servicios_web.repository.ICargoRepository;
import com.marianodefea.servicios_web.service.AgenteCargoService;
import com.marianodefea.servicios_web.service.AgenteService;
import com.marianodefea.servicios_web.service.CargoService;
import com.marianodefea.servicios_web.utils.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/asignaciones")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AsignacionController {

    @Autowired private AgenteCargoService agenteCargoService;
    @Autowired private AgenteService agenteService;
    @Autowired private CargoService cargoService;
    @Autowired private AgenteCargoMapper agenteCargoMapper;

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
            return "redirect:/agentes/";
        }catch(IllegalArgumentException iae) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la asignación");
            redirectAttributes.addFlashAttribute("errorExt", iae.getMessage());
            return "redirect:/admin/asignaciones/crear";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la asignación: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorExt", "" + e.getMessage());
            return "redirect:/agentes/";
        }

    }

    @GetMapping("/agenteCargo/editar/{id}")
    public String mostrarEdicionCargo(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<AgenteCargo> cargoOpt = agenteCargoService.findByIdConRelaciones(id);
        if (cargoOpt.isPresent()){
            ActualizarAgenteCargoDTO dto = agenteCargoMapper.aDto(cargoOpt.get());
            model.addAttribute("cargoEdit", dto);
            return "asignaciones/editar_agente_cargo";
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cargo asignado.");
            return "redirect:/agentes/";
        }
    }

    @PostMapping("/agenteCargo/editar")
    public String guardarEdicionCargo(@ModelAttribute("cargoEdit") ActualizarAgenteCargoDTO dto, RedirectAttributes redirectAttributes){
        try {

            Horario horarioAValdiar = new Horario(dto.getHoraInicio(), dto.getHoraFin());

            boolean haySuperposicion = agenteCargoService.existeSuperposicion(dto.getAgenteId(), horarioAValdiar, dto.getId());

            if (haySuperposicion){
                redirectAttributes.addFlashAttribute("error", "El horario ingresado se superpone con otro cargo activo de este agente.");
                return "redirect:/agentes/";
            }

            Optional<AgenteCargo> cargoRealOpt = agenteCargoService.findById(dto.getId());
            if (cargoRealOpt.isPresent()){
                AgenteCargo cargoReal = cargoRealOpt.get();
                if (cargoReal.getHorario() == null){
                    cargoReal.setHorario(new Horario());
                }

                agenteCargoMapper.actualizarDesdeDto(dto, cargoReal);
                agenteCargoService.save(cargoReal);

                redirectAttributes.addFlashAttribute("success", "Cargo actualizado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar los cambios del cargo.");
        }
        return "redirect:/agentes/ver/" + dto.getAgenteId();
    }
}