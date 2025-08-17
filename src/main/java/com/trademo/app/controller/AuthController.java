package com.trademo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.trademo.app.model.User;

@Controller
public class AuthController {

    // Show the register form page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());  // empty User object for form binding
        return "register";  // looks for register.html inside templates
    }

    // Handle register form submit
    @PostMapping("/register")
    public String processRegister(User user) {
        // Here you would typically save the user to the database
        System.out.println("Registering user: " + user.getName());
        return "redirect:/login";  // after register, go to login page
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // looks for login.html inside templates
    }
}
