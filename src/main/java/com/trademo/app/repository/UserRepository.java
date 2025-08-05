package com.trademo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trademo.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // String is the type of user ID (primary key)
}
