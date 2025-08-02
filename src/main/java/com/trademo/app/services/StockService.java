package com.trademo.app.services;

import org.springframework.stereotype.Service;
import com.trademo.app.model.User;
import com.trademo.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.trademo.app.model.StockTransaction;
import com.trademo.app.repository.TransactionRepository;
import java.time.LocalDateTime;

@Service
public class StockService {

    @Autowired
    private StockApiService stockApiService; // Assuming StockApiService is a service that fetches stock prices;

    public String buyStock(String userId, String stockSymbol, int quantity) {
        // Logic to buy stock

        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalCost = stockPrice * quantity;
        
        User user = UserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - totalCost);
        UserRepository.save(user);

        StockTransaction transaction = new StockTransaction();
        transaction.setUserId(userId);
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(stockPrice);
        transaction.setTransactionType("BUY");
        transaction.setDateTime(LocalDateTime.now());
        TransactionRepository.save(transaction);

        return "Bought " + quantity + " shares of " + stockSymbol;
    }

    public String sellStock(String userId, String stockSymbol, int quantity) {
        // Logic to sell stock

        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalRevenue = stockPrice * quantity;

        User user = UserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBalance(user.getBalance() + totalRevenue);
        UserRepository.save(user);

        StockTransaction transaction = new StockTransaction();
        transaction.setUserId(userId);
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(stockPrice);
        transaction.setTransactionType("SELL");
        transaction.setDateTime(LocalDateTime.now());
        TransactionRepository.save(transaction);

        return "Sold " + quantity + " shares of " + stockSymbol;
    }
}
