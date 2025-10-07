package com.trademo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
// Update the import path if UserService is in a different package, for example:
import com.trademo.app.services.UserService;
// Or, if the class does not exist, create UserService in the correct package: com.trademo.app.services

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private UserService userService;

    // Step 1: Request balance addition (password check + OTP sent)
    @PostMapping("/request-add-balance")
    public ResponseEntity<String> requestAddBalance(
            @RequestParam String email,
            @RequestParam String password) {

        String otp = userService.requestAddBalance(email, password);
        return ResponseEntity.ok("OTP sent to email (for testing: " + otp + ")");
    }

    // Step 2: Confirm OTP & add balance
    @PostMapping("/confirm-add-balance")
    public ResponseEntity<String> confirmAddBalance(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam BigDecimal amount) {

        BigDecimal newBalance = userService.confirmAddBalance(email, otp, amount);
        return ResponseEntity.ok("Balance updated. New balance: " + newBalance);
    }
}
