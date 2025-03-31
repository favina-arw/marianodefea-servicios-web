package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.dto.TipoFichadaDTO;
import com.marianodefea.servicios_web.model.Agente;
import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.model.fichada.TipoFichada;
import com.marianodefea.servicios_web.service.AgenteService;
import com.marianodefea.servicios_web.service.FichadaService;
import com.marianodefea.servicios_web.service.TipoFichadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/fichada")
public class FichadaController {

    @Autowired
    private FichadaService fichadaService;

    @Autowired
    private TipoFichadaService tipoFichadaService;

    @Autowired
    private AgenteService agenteService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public String ingresarFichada(){
        return "public/fichaje";
    }

    @PreAuthorize("permitAll()")
    @PostMapping()
    public String ficharAgente(@RequestParam String dni, Model model){
        if (dni.length() <= 8){
            Optional<Agente> agente = agenteService.findByDni(dni);
            if (agente.isEmpty()) {
                model.addAttribute("error", "No se encuantra Agente con DNI: " + dni + "");
                return "public/fichaje";
            }

            List<Fichada> fichadasDeAgente = agente.get().getFichadas();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (dd/MM/yy)");
            char tipoFichada;
            String mensaje;

            if(fichadasDeAgente.isEmpty()) {
                tipoFichada = 'E';
                mensaje = "¡Bienvenido/a, " + agente.get().getNombre() + "!";

                Fichada nuevaFichada = new Fichada();
                nuevaFichada.setAgente(agente.get());
                nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
                nuevaFichada.setHora(LocalDateTime.now());
                Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
                String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
                model.addAttribute("success", mensaje + " - Hora: " + horaFormateada);
                return "public/fichaje";
            }

            if(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E')){
                System.out.println("Identidicador de última fichada equals 'E'");
                System.out.println(fichadasDeAgente.get(0).getId());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E'));
                tipoFichada = 'S';
                mensaje = "¡Adiós, " + agente.get().getApellido() + ", "+ agente.get().getNombre() + "!";

            }else {
                System.out.println("ELSE - Identidicador de última fichada equals 'S'");
                System.out.println(fichadasDeAgente.get(0).getId());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E'));
                tipoFichada = 'E';
                mensaje = "¡Bienvenido/a, " + agente.get().getApellido() + ", "+ agente.get().getNombre() + "!";
            }

            Fichada nuevaFichada = new Fichada();
            nuevaFichada.setAgente(agente.get());
            nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
            nuevaFichada.setHora(LocalDateTime.now());
            Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
            String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
            model.addAttribute("success", mensaje + " - Hora: " + horaFormateada);
        }else{
            Optional<Agente> agente = agenteService.findByCuil(dni);
            if (agente.isEmpty()) {
                model.addAttribute("error", "No se encuantra Agente con CUIL/T: " + dni + "");
                return "public/fichaje";
            }

            List<Fichada> fichadasDeAgente = agente.get().getFichadas();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (dd/MM/yy)");
            char tipoFichada;
            String mensaje;

            if(fichadasDeAgente.isEmpty()) {
                tipoFichada = 'E';
                mensaje = "¡Bienvenido/a, " + agente.get().getNombre() + "!";

                Fichada nuevaFichada = new Fichada();
                nuevaFichada.setAgente(agente.get());
                nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
                nuevaFichada.setHora(LocalDateTime.now());
                Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
                String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
                model.addAttribute("success", mensaje + " - Hora: " + horaFormateada);
                return "public/fichaje";
            }

            if(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E')){
                System.out.println("Identidicador de última fichada equals 'E'");
                System.out.println(fichadasDeAgente.get(0).getId());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E'));
                tipoFichada = 'S';
                mensaje = "¡Adiós, " + agente.get().getApellido() + ", "+ agente.get().getNombre() + "!";

            }else {
                System.out.println("ELSE - Identidicador de última fichada equals 'S'");
                System.out.println(fichadasDeAgente.get(0).getId());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador());
                System.out.println(fichadasDeAgente.get(0).getTipoFichada().getIdentificador().equals('E'));
                tipoFichada = 'E';
                mensaje = "¡Bienvenido/a, " + agente.get().getApellido() + ", "+ agente.get().getNombre() + "!";
            }

            Fichada nuevaFichada = new Fichada();
            nuevaFichada.setAgente(agente.get());
            nuevaFichada.setTipoFichada(tipoFichadaService.findByIdentificador(tipoFichada).get());
            nuevaFichada.setHora(LocalDateTime.now());
            Fichada fichadaParaRetornar = fichadaService.save(nuevaFichada);
            String horaFormateada = fichadaParaRetornar.getHora().format(formatter);
            model.addAttribute("success", mensaje + " - Hora: " + horaFormateada);
        }
        return "public/fichaje";
    }

    @GetMapping("/crearTipoFichada")
    public String createTipoFichada(){
        return "public/crear_tipofichada";
    }

    @PostMapping("crearTipoFichada")
    public String createTipoFichada(@ModelAttribute TipoFichadaDTO tipoFichadaDTO, Model model){
        TipoFichada nuevoTipo = new TipoFichada();
        nuevoTipo.setIdentificador(tipoFichadaDTO.getIdentificador());
        nuevoTipo.setNombre(tipoFichadaDTO.getNombre());
        TipoFichada tipoResultado = tipoFichadaService.save(nuevoTipo);
        model.addAttribute("success", "Tipo: " + tipoResultado.getNombre() + " - Creado con éxito.");
        return "public/crear_tipofichada";
    }
}
