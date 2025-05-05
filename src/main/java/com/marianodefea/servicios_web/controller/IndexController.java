package com.marianodefea.servicios_web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(){
        return "public/index";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        model.addAttribute("mensaje", "Bienvenido!...123");
        return "user/main_dashboard";
    }
}
