package com.trademo.app.repository;

import com.trademo.app.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public class TransactionRepository extends JpaRepository<StockTransaction, Long> {
    // This interface will automatically provide CRUD operations for StockTransaction entities

}
