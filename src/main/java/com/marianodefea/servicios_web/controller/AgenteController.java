package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.*;
import com.marianodefea.servicios_web.dto.mapper.AgenteCargoMapper;
import com.marianodefea.servicios_web.dto.mapper.AgenteMapper;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.AgenteCargo;
import com.marianodefea.servicios_web.service.AgenteCargoService;
import com.marianodefea.servicios_web.service.AgenteService;
import com.marianodefea.servicios_web.service.CargoService;
import com.marianodefea.servicios_web.utils.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agentes")
public class AgenteController {

    @Autowired
    private AgenteService agenteService;

    @Autowired
    private AgenteMapper agenteMapper;

    @Autowired
    private AgenteCargoService agenteCargoService;

    @Autowired
    private AgenteCargoMapper agenteCargoMapper;

    @Autowired
    private CargoService cargoService;

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
            ActualizarAgenteDTO agenteDTO = agenteMapper.aDto(agenteOptional.get());
            model.addAttribute("agente", agenteDTO);
            return "user/editar_agente";
        }else{
            redirectAttributes.addFlashAttribute("error", "Agente no encontrado");
            return "redirect:/agentes/";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarAgente(@ModelAttribute("agente") ActualizarAgenteDTO dto, RedirectAttributes redirectAttributes) {
        try {
            Optional<Agente> agenteExistenteOpt = agenteService.findById(dto.getId());
            if (agenteExistenteOpt.isPresent()){
                Agente agenteReal = agenteExistenteOpt.get();
                agenteMapper.actualizarAgenteDesdeDto(dto, agenteReal);
                agenteService.save(agenteReal);
                redirectAttributes.addFlashAttribute("success", "El perfil de " + agenteReal.getApellido() + " se actualizó correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se encontró al agente");
                return "redirect:/agentes/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al intentar guardar los cambios.");
        }
        return "redirect:/agentes/ver/" + dto.getId();
    }

    @GetMapping("/asignar_cargo")
    public String formAsignar(Model model) {
        // Le pasamos el DTO vacío a Thymeleaf para que mapee el formulario
        model.addAttribute("asignacionDTO", new AsignacionCargoDTO());

        // Llenamos los desplegables usando los Services, respetando la arquitectura
        model.addAttribute("agentes", agenteService.obtenerTodosLosAgentesDTO());
        model.addAttribute("cargos", cargoService.obtenerActivos());
        return "asignaciones/crear_asignacion";
    }

    @GetMapping("/{id}/asignar_cargo")
    public String formAsignar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Agente> agente = agenteService.findById(id);
            if (agente.isPresent()) {
                model.addAttribute("agente", agente.get());
                model.addAttribute("cargos", cargoService.obtenerActivos());
                model.addAttribute("asignacionDTO", new AsignacionCargoDTO());
            } else {
                // Si el Optional está vacío, redirigimos acá (no entra al catch)
                redirectAttributes.addFlashAttribute("error", "El agente que intenta modificar no se encuentra.");
                return "redirect:/agentes/";
            }
        }catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El agente qie intenta modificar, no se encuentra");
            redirectAttributes.addFlashAttribute("errorExt", "" + e.getMessage());
            return "redirect:/agentes/";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al intentar asignar el agente");
            redirectAttributes.addFlashAttribute("errorExt", "" + e.getMessage());
            return "redirect:/agentes/";
        }
        return "asignaciones/crear_asignacion_bis";
    }


    @PostMapping("/guardar_asignacion")
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
