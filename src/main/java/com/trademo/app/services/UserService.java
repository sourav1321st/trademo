package com.trademo.app.services;

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
}
