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

    private static final String TYPE_BUY = "BUY";
    private static final String TYPE_SELL = "SELL";

    @Autowired
    private StockApiService stockApiService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public String buyStock(String userId, String stockSymbol, int quantity) {
        validateQuantity(quantity);
        User user = getUserById(userId);

        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalCost = stockPrice * quantity;

        if (user.getVirtualBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance to buy " + quantity + " shares.");
        }

        // Deduct balance
        user.setVirtualBalance(user.getVirtualBalance() - totalCost);
        userRepository.save(user);

        // Save transaction
        saveTransaction(user, stockSymbol, quantity, stockPrice, TYPE_BUY);

        return "✅ Bought " + quantity + " shares of " + stockSymbol + " at ₹" + stockPrice;
    }

    public double getStockPrice(String symbol) {
        return stockApiService.getCurrentPrice(symbol);
    }

    public String sellStock(String userId, String stockSymbol, int quantity) {

        System.out.println("Sell method started in StockService for stock: " + stockSymbol);//-->>>>>>>>>>>>>>>>

        validateQuantity(quantity);
        User user = getUserById(userId);

        // Step 1: Check how many shares the user owns
        int totalOwned = getTotalOwnedShares(user, stockSymbol);
        if (totalOwned < quantity) {
            throw new IllegalArgumentException("Not enough shares to sell. You own only " + totalOwned);
        }

        // Step 2: Get current stock price
        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalRevenue = stockPrice * quantity;

        // Step 3: Update balance
        user.setVirtualBalance(user.getVirtualBalance() + totalRevenue);
        userRepository.save(user);

        // Save transaction
        saveTransaction(user, stockSymbol, quantity, stockPrice, TYPE_SELL);

        return "✅ Sold " + quantity + " shares of " + stockSymbol
                + " at ₹" + stockPrice + " | Total Revenue: ₹" + totalRevenue;
    }

    // ===== Helper Methods =====
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    // Fetch user by ID and handle exceptions
    private User getUserById(String userId) {
        try {
            @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
            Long id = Long.parseLong(userId);
            return userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
    }

    // Calculate total shares owned by the user for a specific stock
    private int getTotalOwnedShares(User user, String stockSymbol) {
        return transactionRepository.findByUserAndStockSymbol(user, stockSymbol).stream()
                .mapToInt(t -> t.getType().equalsIgnoreCase("BUY") ? t.getQuantity() : -t.getQuantity())
                .sum();
    }

    // Save a stock transaction
    private void saveTransaction(User user, String stockSymbol, int quantity, double price, String type) {
        StockTransaction transaction = new StockTransaction();
        transaction.setUser(user);
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setType(type);
        transaction.setDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
