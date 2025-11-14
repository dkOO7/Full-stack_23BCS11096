package com.tradingapp.trading_platform.controller;

import com.tradingapp.trading_platform.dto.PortfolioResponseDto;
import com.tradingapp.trading_platform.model.Portfolio;
import com.tradingapp.trading_platform.model.PortfolioValueHistory;
import com.tradingapp.trading_platform.repository.PortfolioRepository;
import com.tradingapp.trading_platform.repository.PortfolioValueHistoryRepository;
import com.tradingapp.trading_platform.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioValueHistoryRepository portfolioValueHistoryRepository;

    @Autowired
    public PortfolioController(PortfolioService portfolioService,
                               PortfolioRepository portfolioRepository,
                               PortfolioValueHistoryRepository portfolioValueHistoryRepository) {
        this.portfolioService = portfolioService;
        this.portfolioRepository = portfolioRepository;
        this.portfolioValueHistoryRepository = portfolioValueHistoryRepository;
    }

    // ✅ 1️⃣ Get current portfolio summary
    @GetMapping
    public ResponseEntity<PortfolioResponseDto> getUserPortfolio() {
        PortfolioResponseDto portfolio = portfolioService.getPortfolioForCurrentUser();
        return ResponseEntity.ok(portfolio);
    }

    // ✅ 2️⃣ Get historical portfolio values
    @GetMapping("/history")
    public ResponseEntity<?> getPortfolioHistory(Authentication auth) {
        String username = auth.getName();

        Portfolio portfolio = portfolioRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for user: " + username));

        List<PortfolioValueHistory> history =
                portfolioValueHistoryRepository.findByPortfolioIdOrderByDateAsc(portfolio.getId());

        return ResponseEntity.ok(history);
    }
}
