package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.model.Materia;
import com.marianodefea.servicios_web.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/materias")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping("/")
    public String listarMaterias(Model model) {
        List<Materia> materias = materiaService.obtenerTodas();
        model.addAttribute("materias", materias);
        return "materias/listar_materias";
    }

    @GetMapping("/alta/{id}")
    public String activarMateria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            materiaService.cambiarEstado(id, true);
            redirectAttributes.addFlashAttribute("success", "Materia activada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar la materia.");
        }
        return "redirect:/materias/";
    }

    @GetMapping("/baja/{id}")
    public String desactivarMateria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            materiaService.cambiarEstado(id, false);
            redirectAttributes.addFlashAttribute("success", "Materia dada de baja correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al dar de baja la materia.");
        }
        return "redirect:/materias/";
    }
}