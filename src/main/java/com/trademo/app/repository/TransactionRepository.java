package com.trademo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trademo.app.model.StockTransaction;
import com.trademo.app.model.User;

@Repository
public interface TransactionRepository extends JpaRepository<StockTransaction, Long> {
// This interface will automatically provide CRUD operations for StockTransaction entities

     List<StockTransaction> findByUser(User user);

    // Optional: You can also filter by stock symbol if needed
    List<StockTransaction> findByUserAndStockSymbol(User user, String stockSymbol);

    // Optional: Find all transactions of a user by type (BUY/SELL)
    List<StockTransaction> findByUserAndType(User user, String type);
    



}


