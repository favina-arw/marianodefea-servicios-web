package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.AsistenciaPorAgenteDTO;
import com.marianodefea.servicios_web.dto.FichadaDTO;
import com.marianodefea.servicios_web.dto.TipoFichadaDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.model.fichada.TipoFichada;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import com.marianodefea.servicios_web.service.AgenteService;
import com.marianodefea.servicios_web.service.FichadaService;
import com.marianodefea.servicios_web.service.InformeService;
import com.marianodefea.servicios_web.service.TipoFichadaService;
import com.marianodefea.servicios_web.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fichada")
public class FichadaController {

    @Autowired
    private FichadaService fichadaService;

    @Autowired
    private TipoFichadaService tipoFichadaService;

    @Autowired
    private AgenteService agenteService;

    @Autowired
    private InformeService informeService;

    @GetMapping("/listarFichadas")
    public String listarFichadas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoFichada,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String dni,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("hora").descending());
        Page<Fichada> fichadas = fichadaService.buscarFichadas(nombre, apellido, tipoFichada, fechaDesde, fechaHasta, dni, pageable);

        model.addAttribute("fichadas", fichadas);
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("tipoFichada", tipoFichada);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);
        model.addAttribute("dni", dni);

        return "user/listar_fichadas";
    }

    @GetMapping("/informeAsistenciaMensual")
    public String mostrarInforme(Model model){
        List<AsistenciaPorAgenteDTO> informe = informeService.generarInformeAsistenciaMensual();
        LocalDate inicio = DateUtils.getPrimerDiaDelMesAnterior();
        LocalDate fin = DateUtils.getUltimoDiaDelMesAnterior();

        List<LocalDate> diasHabiles = DateUtils.getDiasLaborablesPorRango(inicio, fin);
        model.addAttribute("diasHabiles", diasHabiles);
        model.addAttribute("informe", informe);
        return "user/mostrar_informe_asistencia_mensual";
    }

    @GetMapping("/informeAsistenciaMensual/{mes}")
    public String mostrarInforme(Model model, @PathVariable("mes") int numeroMes){
        List<AsistenciaPorAgenteDTO> informe = informeService.generarInformeAsistenciaMensual();
        LocalDate inicio = DateUtils.getPrimerDiaDelMesAnterior();
        LocalDate fin = DateUtils.getUltimoDiaDelMesAnterior();

        List<LocalDate> diasHabiles = DateUtils.getDiasLaborablesPorRango(inicio, fin);
        model.addAttribute("diasHabiles", diasHabiles);
        model.addAttribute("informe", informe);
        return "user/mostrar_informe_asistencia_mensual";
    }

    @GetMapping("/crearTipoFichada")
    public String createTipoFichada(){
        return "user/crear_tipofichada";
    }

    @PostMapping("/crearTipoFichada")
    public String createTipoFichada(@ModelAttribute TipoFichadaDTO tipoFichadaDTO, Model model){
        TipoFichada nuevoTipo = new TipoFichada();
        nuevoTipo.setIdentificador(tipoFichadaDTO.getIdentificador());
        nuevoTipo.setNombre(tipoFichadaDTO.getNombre());
        TipoFichada tipoResultado = tipoFichadaService.save(nuevoTipo);
        model.addAttribute("success", "Tipo: " + tipoResultado.getNombre() + " - Creado con éxito.");
        return "user/crear_tipofichada";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/fichador")
    public String ingresarFichada(Model model){
        if (model.containsAttribute("in")) {
            String atributoIn = (String) model.getAttribute("in");
            model.addAttribute("in", atributoIn); // Vuelve a agregar al modelo para la vista
        } else if (model.containsAttribute("out")) {
            String atributoOut = (String) model.getAttribute("out");
            model.addAttribute("out", atributoOut);
        } else if (model.containsAttribute("error")) {
            String atributoError = (String) model.getAttribute("error");
            model.addAttribute("error", atributoError);
        }

        List<Fichada> ultimasQuince = fichadaService.findTop10ByOrderByHoraDesc()
                        .stream()
                        .peek(fichada -> {
                            fichada.getAgente().setNombre(fichada.getAgente().getNombre().toUpperCase());
                            fichada.getAgente().setApellido(fichada.getAgente().getApellido().toUpperCase());
                        }).collect(Collectors.toList());
        System.out.println(ultimasQuince);
        model.addAttribute("ultimasQuince", ultimasQuince);
        return "public/fichaje";
    }

    @PreAuthorize("permitAll()")
    @PostMapping()
    public String ficharAgente(@RequestParam String dni, Model model, RedirectAttributes redirectAttributes){
        if (dni.length() <= 8){
            Optional<Agente> agente = agenteService.findByDni(dni);
            if (agente.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se encuantra Agente con DNI: " + dni + "");
                return "redirect:/fichada/fichador";
            }

            List<Fichada> fichadasDeAgente = agente.get().getFichadas();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (dd/MM/yy)");
            char tipoFichada;
            String mensaje;

            if(fichadasDeAgente.isEmpty()) {
                tipoFichada = 'E';
                mensaje = "¡Hola! ¡" + agente.get().getApellido().toUpperCase() + ", " + agente.get().getNombre().toUpperCase() + "!";

                Fichada nuevaFichada = new Fichada();
                nuevaFichada.setAgente(agente.get());
                nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
                nuevaFichada.setHora(LocalDateTime.now());
                Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
                String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
                redirectAttributes.addFlashAttribute("in", mensaje + "\nHora: " + horaFormateada);
                return "redirect:/fichada/fichador";
            }

            if(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E')){
                tipoFichada = 'S';
                mensaje = "¡Adiós! ¡" + agente.get().getApellido().toUpperCase() + ", "+ agente.get().getNombre().toUpperCase() + "!";
            }else {
                tipoFichada = 'E';
                mensaje = "¡Hola! ¡" + agente.get().getApellido().toUpperCase() + ", "+ agente.get().getNombre().toUpperCase() + "!";
            }

            Fichada nuevaFichada = new Fichada();
            nuevaFichada.setAgente(agente.get());
            nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
            nuevaFichada.setHora(LocalDateTime.now());
            Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
            String horaFormateada = fichadaParaRetornar.getHora().format(formatter);

            if(fichadaParaRetornar.getTipoFichada().getIdentificador().equals('E')) {
                redirectAttributes.addFlashAttribute("in", mensaje + "\nHora: " + horaFormateada);
            }else {
                redirectAttributes.addFlashAttribute("out", mensaje + "\nHora: " + horaFormateada);
            }
        }else{
            Optional<Agente> agente = agenteService.findByCuil(dni);
            if (agente.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se encuantra Agente con DNI: " + dni + "");
                return "redirect:/fichada/fichador";
            }

            List<Fichada> fichadasDeAgente = agente.get().getFichadas();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (dd/MM/yy)");
            char tipoFichada;
            String mensaje;

            if(fichadasDeAgente.isEmpty()) {
                tipoFichada = 'E';
                mensaje = "¡Hola! ¡" + agente.get().getApellido().toUpperCase() + ", " + agente.get().getNombre().toUpperCase() + "!";

                Fichada nuevaFichada = new Fichada();
                nuevaFichada.setAgente(agente.get());
                nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
                nuevaFichada.setHora(LocalDateTime.now());
                Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
                String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
                redirectAttributes.addFlashAttribute("in", mensaje + "\nHora: " + horaFormateada);
                return "redirect:/fichada/fichador";
            }

            if(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E')){
                tipoFichada = 'S';
                mensaje = "¡Adiós! ¡" + agente.get().getApellido().toUpperCase() + ", "+ agente.get().getNombre().toUpperCase() + "!";

            }else {
                tipoFichada = 'E';
                mensaje = "¡Hola! ¡" + agente.get().getApellido().toUpperCase() + ", " + agente.get().getNombre().toUpperCase() + "!";
            }

            Fichada nuevaFichada = new Fichada();
            nuevaFichada.setAgente(agente.get());
            nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
            nuevaFichada.setHora(LocalDateTime.now());
            Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
            String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
            if(fichadaParaRetornar.getTipoFichada().getIdentificador().equals('E')) {
                redirectAttributes.addFlashAttribute("in", mensaje + "\nHora: " + horaFormateada);
            }else {
                redirectAttributes.addFlashAttribute("out", mensaje + "\nHora: " + horaFormateada);
            }
        }
        return "redirect:/fichada/fichador";
    }

}
