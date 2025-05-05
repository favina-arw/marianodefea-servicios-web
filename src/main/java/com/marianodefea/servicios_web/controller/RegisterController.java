package com.marianodefea.servicios_web.controller;

import com.marianodefea.servicios_web.model.security.UserSec;
import com.marianodefea.servicios_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "public/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String cuil,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "public/register";
        }

        if (userService.existsByCuil(cuil)) {
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "public/register";
        }

        UserSec newUser = new UserSec();
        newUser.setCuil(cuil);
        newUser.setPassword(userService.encryptPassword(password));
        userService.save(newUser);

        model.addAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "public/register";
    }
}