package com.tradingapp.trading_platform.controller;

import com.tradingapp.trading_platform.model.User; // <-- Capitalized
import com.tradingapp.trading_platform.service.UserService; // <-- Capitalized
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService; // <-- Corrected variable name

    @Autowired
    public UserController(UserService userService) { // <-- Corrected parameter name
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) { // <-- Capitalized User type
        return userService.createUser(user);
    }
}