package com.marianodefea.servicios_web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public String adminHome(){
        return "admin/home";
    }
}
