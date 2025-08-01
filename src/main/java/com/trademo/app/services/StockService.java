package com.trademo.app.services;

import org.springframework.stereotype.Service;
import com.trademo.app.model.User;
import com.trademo.app.repository.userRepository;

@Service
public class StockService {

    public String buyStock(String userId, String stockSymbol, int quantity) {
        // Logic to buy stock

        double stockPrice = stockApiService.getCurrentPrice(stockSymbol);
        double totalCost = stockPrice * quantity;
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - totalCost);
        userRepository.save(user);

        StockTransaction transaction = new StockTransaction();
        transaction.setUserId(userId);
        transaction.setStockSymbol(stockSymbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(stockPrice);
        transaction.setTransactionType("BUY");
        transaction.setDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Bought " + quantity + " shares of " + stockSymbol;
    }
}
