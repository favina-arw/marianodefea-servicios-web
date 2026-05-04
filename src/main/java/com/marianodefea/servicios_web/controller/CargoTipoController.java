package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.model.CargoTipo;
import com.marianodefea.servicios_web.service.CargoTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/cargo_tipos")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CargoTipoController {

    @Autowired
    private CargoTipoService cargoTipoService;

    @GetMapping("/")
    public String listarCargoTipos(Model model) {
        List<CargoTipo> tipos = cargoTipoService.obtenerTodos();
        model.addAttribute("tipos", tipos);
        return "cargo_tipos/listar_cargo_tipos";
    }

    @GetMapping("/alta/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cargoTipoService.cambiarEstado(id, true);
        redirectAttributes.addFlashAttribute("success", "Tipo de cargo activado.");
        return "redirect:/admin/cargo_tipos/";
    }

    @GetMapping("/baja/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cargoTipoService.cambiarEstado(id, false);
        redirectAttributes.addFlashAttribute("success", "Tipo de cargo desactivado.");
        return "redirect:/admin/cargo_tipos/";
    }

    // Mostrar el formulario
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("cargoTipo", new CargoTipo());
        return "cargo_tipos/crear_cargo_tipo";
    }

    // Procesar el guardado
    @PostMapping("/guardar")
    public String guardarCargoTipo(CargoTipo cargoTipo, RedirectAttributes redirectAttributes) {
        try {
            cargoTipoService.guardar(cargoTipo);
            redirectAttributes.addFlashAttribute("success", "Tipo de cargo guardado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el tipo de cargo. ¿Quizás el nombre ya existe?");
        }
        return "redirect:/admin/cargo_tipos/";
    }
}