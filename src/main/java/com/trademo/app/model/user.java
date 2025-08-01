package com.trademo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double virtualBalance;

    //Constructor
    public User() {}

    public User(String name , double virtualBalance) {
        this.name = name;
        this.virtualBalance = virtualBalance;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getVirtualBalance() {
        return virtualBalance;
    }
    public void setVirtualBalance(double virtualBalance) {
        this.virtualBalance = virtualBalance;
    }
}