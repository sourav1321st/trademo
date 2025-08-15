package com.trademo.app.dto;

public class SellRequest {
    private Long userId;
    private String stockSymbol;
    private int quantity;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
