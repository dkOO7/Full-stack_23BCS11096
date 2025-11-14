package com.tradingapp.trading_platform.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController{

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService= authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        String jwt = authService.login(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
