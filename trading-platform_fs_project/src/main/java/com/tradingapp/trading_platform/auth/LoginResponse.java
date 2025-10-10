package com.tradingapp.trading_platform.auth;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwt;
    public LoginResponse(String jwt){
        this.jwt=jwt;
    }
}
