package com.tradingapp.trading_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tradingapp.trading_platform.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // Spring Data JPA will automatically create methods like save(), findById(), etc.
    // We can add custom queries here later if needed.

    Optional<User> findByUsername(String username);
}
