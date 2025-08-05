package com.trademo.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trademo.app.dto.BuyRequest;
import com.trademo.app.services.StockService;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    // Injecting the StockService to handle real buy logic
    @Autowired
    private StockService stockService;

    // This list will temporarily act like a database (demo purpose)
    private List<String> virtualTrades = new ArrayList<>();

    // GET method: See all virtual trades (temporary DB)
    @GetMapping
    public List<String> getAllTrades() {
        return virtualTrades;
    }

    // POST method: Add a new trade (for demo only, stores raw string)
    @PostMapping
    public String createTrade(@RequestBody String trade) {
        virtualTrades.add(trade);
        return "Trade added successfully: " + trade;
    }

    // DELETE method: Remove trade by index from temporary list
    @DeleteMapping("/{index}")
    public String deleteTrade(@PathVariable int index) {
        if (index >= 0 && index < virtualTrades.size()) {
            String removed = virtualTrades.remove(index);
            return "Trade removed: " + removed;
        } else {
            return "Invalid index";
        }
    }

    // POST method: Real API for buying stock using virtual balance
    @PostMapping("/buy")
    public String buyStock(@RequestBody BuyRequest request) {
        try {
            return stockService.buyStock(
                String.valueOf(request.getUserId()),
                request.getStockSymbol(),
                request.getQuantity()
            );
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
