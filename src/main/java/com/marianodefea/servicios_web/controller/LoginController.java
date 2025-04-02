package com.marianodefea.servicios_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "errorMessage", required = false) String errorMessage, Model model){
        model.addAttribute("title", "Login");
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage); // Agrega el mensaje al modelo
        }
        return "login";
    }

}
