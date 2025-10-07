package com.trademo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trademo.app.model.User;
import com.trademo.app.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

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
        userRepository.save(user);
        System.out.println("Registering user: " + user.getName());
        return "redirect:/login";  // after register, go to login page
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // loads templates/login.html
    }

    // Handle login form submit
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser != null && existingUser.getPassword().equals(password)) {

            session.setAttribute("loggedInUser", existingUser); // ✅ store user in session

            // ✅ Put user details in model for dashboard
            model.addAttribute("user", existingUser);
            return "dashboard";  // directly load dashboard with user data
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // ✅ clears session data
        return "redirect:/login?logout"; // redirect to login with a logout message
    }

}
