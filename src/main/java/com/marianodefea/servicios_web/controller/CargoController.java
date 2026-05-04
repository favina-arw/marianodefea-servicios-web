package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.model.Cargo;
import com.marianodefea.servicios_web.service.CargoService;
import com.marianodefea.servicios_web.service.CargoTipoService;
import com.marianodefea.servicios_web.service.CursoService;
import com.marianodefea.servicios_web.service.MateriaService;
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
@RequestMapping("/admin/cargos")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @Autowired
    private CargoTipoService cargoTipoService;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private CursoService cursoService;

    @GetMapping("/")
    public String listarCargos(Model model) {
        List<Cargo> cargos = cargoService.obtenerTodos();
        model.addAttribute("cargos", cargos);
        return "cargos/listar_cargos";
    }

    @GetMapping("/alta/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cargoService.cambiarEstado(id, true);
        redirectAttributes.addFlashAttribute("success", "Estructura de cargo activada.");
        return "redirect:/admin/cargos/";
    }

    @GetMapping("/baja/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cargoService.cambiarEstado(id, false);
        redirectAttributes.addFlashAttribute("success", "Estructura de cargo desactivada.");
        return "redirect:/admin/cargos/";
    }

    // Mostrar el formulario
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("cargo", new Cargo());

        // Le pasamos las listas para armar los <select>
        model.addAttribute("tipos", cargoTipoService.obtenerActivos());
        model.addAttribute("materias", materiaService.obtenerActivas());
        model.addAttribute("cursos", cursoService.obtenerActivos());

        return "cargos/crear_cargo";
    }

    // Procesar el guardado
    @PostMapping("/guardar")
    public String guardarCargo(Cargo cargo, RedirectAttributes redirectAttributes) {
        try {
            cargoService.guardar(cargo);
            redirectAttributes.addFlashAttribute("success", "Estructura de cargo creada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el cargo.");
        }
        return "redirect:/admin/cargos/";
    }
}