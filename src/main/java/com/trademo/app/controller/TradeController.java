package com.trademo.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    // Your methods will go here

    // This list will temporarily act like a database
    private List<String> virtualTrades = new ArrayList<>();

    // GET method: See all virtual trades
    @GetMapping
    public List<String> getAllTrades() {
        return virtualTrades;
    }

    // POST method: Add a new trade (like buying a stock)
    @PostMapping
    public String createTrade(@RequestBody String trade) {
        virtualTrades.add(trade);
        return "Trade added successfully: " + trade;
    }

    // DELETE method: Delete a trade using index (just for demo)
    @DeleteMapping("/{index}")
    public String deleteTrade(@PathVariable int index) {
        if (index >= 0 && index < virtualTrades.size()) {
            String removed = virtualTrades.remove(index);
            return "Trade removed: " + removed;
        } else {
            return "Invalid index";
        }
    }
}

