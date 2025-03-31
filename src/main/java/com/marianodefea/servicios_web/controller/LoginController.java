package com.marianodefea.servicios_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

}
