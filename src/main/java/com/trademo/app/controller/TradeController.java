package com.trademo.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trademo.app.services.StockService;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private StockService stockService;

    private final List<String> virtualTrades = new ArrayList<>();

    @GetMapping("/")
    public String home() {
        return "Welcome to Trademo! Your virtual stock trading platform is up and running.";
    }

    @GetMapping
    public List<String> getAllTrades() {
        return virtualTrades;
    }

    @GetMapping("/test-stock/{symbol}")
    public String testStockPrice(@PathVariable String symbol) {
        try {
            double price = stockService.getStockPrice(symbol);
            return "Current price of " + symbol + " is ₹" + price;
        } catch (Exception e) {
            return "Error fetching price: " + e.getMessage();
        }
    }

    @GetMapping("/price/{symbol}")
    public String getStockPrice(@PathVariable String symbol) {
        try {
            double price = stockService.getStockPrice(symbol);
            return "Current price of " + symbol.toUpperCase() + " is ₹" + price;
        } catch (Exception e) {
            return "Error fetching price: " + e.getMessage();
        }
    }

    // CREATE TRADE (now GET with params)
    @GetMapping("/create")
    public String createTrade(@RequestParam String trade) {
        virtualTrades.add(trade);
        return "Trade added successfully: " + trade;
    }

    @DeleteMapping("/{index}")
    public String deleteTrade(@PathVariable int index) {
        if (index >= 0 && index < virtualTrades.size()) {
            String removed = virtualTrades.remove(index);
            return "Trade removed: " + removed;
        } else {
            return "Invalid index";
        }
    }

    // SELL STOCK (GET for browser)
    @GetMapping("/sell")
    public ResponseEntity<String> sellStock(
            @RequestParam String userId,
            @RequestParam String stockSymbol,
            @RequestParam int quantity) {
        String result = stockService.sellStock(userId, stockSymbol, quantity);
        System.out.println("Sell method started for " + stockSymbol);
        return ResponseEntity.ok(result);
    }

    // BUY STOCK (GET for browser)
    @GetMapping("/buy")
    public String buyStock(
            @RequestParam String userId,
            @RequestParam String stockSymbol,
            @RequestParam int quantity) {
        try {
            return stockService.buyStock(userId, stockSymbol, quantity);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
