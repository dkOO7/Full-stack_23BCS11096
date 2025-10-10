package com.tradingapp.trading_platform.service;

import com.tradingapp.trading_platform.dto.HoldingDto;
import com.tradingapp.trading_platform.dto.PortfolioResponseDto;
import com.tradingapp.trading_platform.model.Holding;
import com.tradingapp.trading_platform.model.Portfolio;
import com.tradingapp.trading_platform.model.User;
import com.tradingapp.trading_platform.repository.HoldingRepository;
import com.tradingapp.trading_platform.repository.PortfolioRepository;
import com.tradingapp.trading_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;
    private final MarketDataService marketDataService;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository,
                            UserRepository userRepository,
                            HoldingRepository holdingRepository,
                            MarketDataService marketDataService) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
        this.marketDataService = marketDataService;
    }

    public PortfolioResponseDto getPortfolioForCurrentUser() {
        // Get the current user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Get their portfolio
        Portfolio portfolio = portfolioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found for user: " + username));

        // Get all their stock holdings
        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolio.getId());

        PortfolioResponseDto response = new PortfolioResponseDto();
        response.setCashBalance(portfolio.getCashBalance());

        List<HoldingDto> holdingDtos = new ArrayList<>();
        BigDecimal totalStockValue = BigDecimal.ZERO;

        // For each holding, fetch the live price and create a DTO
        for (Holding holding : holdings) {
            HoldingDto dto = new HoldingDto();
            String symbol = holding.getStock().getSymbol();
            dto.setSymbol(symbol);
            dto.setQuantity(holding.getQuantity());

            if (symbol == null || symbol.trim().isEmpty()) {
                dto.setCurrentPrice(BigDecimal.ZERO);
                dto.setMarketValue(BigDecimal.ZERO);
            } else {
                BigDecimal currentPrice = marketDataService.getLivePrice(symbol.trim());
                BigDecimal marketValue = currentPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));
                dto.setCurrentPrice(currentPrice);
                dto.setMarketValue(marketValue);
                totalStockValue = totalStockValue.add(marketValue);
            }

            holdingDtos.add(dto);
        }

        response.setHoldings(holdingDtos);
        response.setTotalStockValue(totalStockValue);
        response.setTotalPortfolioValue(portfolio.getCashBalance().add(totalStockValue));

        return response;
    }
}
