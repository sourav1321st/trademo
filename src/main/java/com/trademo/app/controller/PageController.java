package com.trademo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/home")
    public String homePage() {
        return "home"; // maps to index.html in templates
    }

    @GetMapping("/trade")
    public String tradePage() {
        return "trade";  // maps to trade.html in templates
    }

     @GetMapping("/portfolio")
    public String showPortfolioPage() {
        return "portfolio"; // Looks for portfolio.html
    }

    @GetMapping("/dashboard")
    public String showDashboardPage() {
        return "dashboard"; // Maps to dashboard.html
    }

}
