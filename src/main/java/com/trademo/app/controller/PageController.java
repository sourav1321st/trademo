package com.trademo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home"; // maps to index.html in templates
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Maps to dashboard.html
    }

    @GetMapping("/trade")
    public String trade() {
        return "trade";  // maps to trade.html in templates
    }

    @GetMapping("/portfolio")
    public String portfolio() {
        return "portfolio"; // Looks for portfolio.html
    }

    @GetMapping("/wallet")
    public String walletPage() {
        return "wallet"; // loads wallet.html
    }

}
