package com.tradingapp.trading_platform.service;

import com.tradingapp.trading_platform.model.Portfolio;
import com.tradingapp.trading_platform.model.User;
import com.tradingapp.trading_platform.repository.PortfolioRepository; // <-- Add this import
import com.tradingapp.trading_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Add this import

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository; // <-- Add this
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PortfolioRepository portfolioRepository, PasswordEncoder passwordEncoder) { // <-- Add to constructor
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository; // <-- Add this
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional // <-- Add this annotation
    public User createUser(User user) {
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 1. Save the new user to get their generated ID
        User savedUser = userRepository.save(user);

        // 2. Create a new portfolio for this user
        Portfolio newPortfolio = new Portfolio();
        newPortfolio.setUser(savedUser);
        newPortfolio.setCashBalance(new BigDecimal("100000.00")); // Give them $100,000 virtual cash

        // 3. Save the new portfolio
        portfolioRepository.save(newPortfolio);

        return savedUser;
    }
}