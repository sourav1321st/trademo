package com.trademo.app.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trademo.app.model.StockTransaction;
import com.trademo.app.model.User;
import com.trademo.app.repository.TransactionRepository;
import com.trademo.app.repository.UserRepository;

@Service
public class StockService {

    @Autowired
    private StockApiService stockApiService; // External API for live stock prices

    @Autowired
    private UserRepository userRepository;  // Repository to access User table

    @Autowired
    private TransactionRepository transactionRepository;  // To save transactions

    // 💰 BUY STOCK METHOD
    public String buyStock(String userId, String stockSymbol, int quantity) {

        // Step 1: Get the current price of the stock
        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);  // ✅ stock price fetched
        double totalCost = stockPrice * quantity;  // ✅ total paisa needed to buy

        // Step 2: Find user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));  // ✅ handle null case

        // Step 3: Check if user has enough balance
        if (user.getVirtualBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");  // ✅ not enough funds
        }

        // Step 4: Deduct amount from user's balance
        user.setVirtualBalance(user.getVirtualBalance() - totalCost);  // ✅ minus the total cost
        userRepository.save(user);  // ✅ save the updated balance to DB

        // Step 5: Create and save stock transaction
        StockTransaction transaction = new StockTransaction();
        transaction.setUser(user);  // ✅ user entity, not just ID
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(stockPrice);
        transaction.setType("BUY");  // ✅ BUY type
        transaction.setDateTime(LocalDateTime.now());

        transactionRepository.save(transaction);  // ✅ Save to DB

        return "Successfully bought " + quantity + " shares of " + stockSymbol + " at ₹" + stockPrice;
    }

    // 💸 SELL STOCK METHOD
    public String sellStock(String userId, String stockSymbol, int quantity) {

        // Step 1: Get stock price
        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalRevenue = stockPrice * quantity;

        // Step 2: Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 3: Add money to user's balance
        user.setVirtualBalance(user.getVirtualBalance() + totalRevenue);
        userRepository.save(user);

        // Step 4: Save transaction
        StockTransaction transaction = new StockTransaction();
        transaction.setUser(user);
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(stockPrice);
        transaction.setType("SELL");
        transaction.setDateTime(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Successfully sold " + quantity + " shares of " + stockSymbol + " at ₹" + stockPrice;
    }
}