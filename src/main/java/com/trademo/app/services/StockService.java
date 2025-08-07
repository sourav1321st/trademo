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

    public String buyStock(String userId, String stockSymbol, int quantity) {
        // Step 0: Validate quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Step 1: Get current price
        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalCost = stockPrice * quantity;

        // Step 2: Find user by ID (converted safely from String to Long)
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 3: Check for sufficient balance
        if (user.getVirtualBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");
        }

        // Step 4: Deduct balance
        double updatedBalance = user.getVirtualBalance() - totalCost;
        user.setVirtualBalance(updatedBalance);
        userRepository.save(user);

        // Step 5: Record the transaction
        StockTransaction transaction = new StockTransaction();
        transaction.setUser(user);
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(stockPrice);
        transaction.setType("BUY");
        transaction.setDateTime(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "✅ Successfully bought " + quantity + " shares of " + stockSymbol + " at ₹" + stockPrice;
    }

    public double getStockPrice(String symbol) {
        return stockApiService.getCurrentPrice(symbol); // Assuming stockApiService is already injected
    }

    public String sellStock(String userId, String stockSymbol, int quantity) {
        // Step 0: Validate input
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Step 1: Get current stock price
        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalRevenue = stockPrice * quantity;

        // Step 2: Fetch user by ID (parsed from String to Long)
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔁 (Optional) You could add logic to check if the user actually owns enough of the stock to sell
        // Step 3: Add money to user's balance
        double updatedBalance = user.getVirtualBalance() + totalRevenue;
        user.setVirtualBalance(updatedBalance);
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

        return "✅ Successfully sold " + quantity + " shares of " + stockSymbol + " at ₹" + stockPrice;
    }

}
