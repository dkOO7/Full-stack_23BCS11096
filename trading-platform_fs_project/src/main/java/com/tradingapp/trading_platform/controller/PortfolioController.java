package com.tradingapp.trading_platform.controller;

import com.tradingapp.trading_platform.dto.PortfolioResponseDto; // <-- Change import
import com.tradingapp.trading_platform.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public ResponseEntity<PortfolioResponseDto> getUserPortfolio() { // <-- Change return type
        PortfolioResponseDto portfolio = portfolioService.getPortfolioForCurrentUser();
        return ResponseEntity.ok(portfolio);
    }
}