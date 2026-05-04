package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.model.Curso;
import com.marianodefea.servicios_web.service.CursoService;
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
@RequestMapping("/admin/cursos")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping("/")
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoService.obtenerTodos();
        model.addAttribute("cursos", cursos);
        return "cursos/listar_cursos";
    }

    @GetMapping("/alta/{id}")
    public String activarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.cambiarEstado(id, true);
            redirectAttributes.addFlashAttribute("success", "Curso activado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar el curso.");
        }
        return "redirect:/cursos/";
    }

    @GetMapping("/baja/{id}")
    public String desactivarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.cambiarEstado(id, false);
            redirectAttributes.addFlashAttribute("success", "Curso dado de baja correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al dar de baja el curso.");
        }
        return "redirect:/cursos/";
    }
}