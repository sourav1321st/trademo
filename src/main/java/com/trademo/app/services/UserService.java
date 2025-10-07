package com.trademo.app.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trademo.app.model.User;
import com.trademo.app.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Method to save a user
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Optional: fetch all users
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Step 1: Generate OTP & verify password
    public String requestAddBalance(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        // Generate OTP (6-digit)
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        // In real app: send OTP via email
        return otp;  // for now, just return OTP
    }

// Step 2: Verify OTP & update balance
    public BigDecimal confirmAddBalance(String email, String otp, BigDecimal amount) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // Add balance
        BigDecimal newBalance = user.getBalance().add(amount);
        user.setBalance(newBalance);

        // Clear OTP
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);
        return newBalance;
    }
}
