package com.tradingapp.trading_platform.controller;

import com.tradingapp.trading_platform.model.Transaction;
import com.tradingapp.trading_platform.service.TradingService;
import org.slf4j.Logger; // <-- Add this import
import org.slf4j.LoggerFactory; // <-- Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradingController {

    // Add this logger
    private static final Logger logger = LoggerFactory.getLogger(TradingController.class);

    private final TradingService tradingService;

    @Autowired
    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("/buy/{symbol}")
    public ResponseEntity<Transaction> buyStock(
            @PathVariable String symbol,
            @RequestParam int quantity,
            Authentication authentication) {

        String username = authentication.getName();
        logger.info(">>> TRADE REQUEST: User '{}' wants to BUY {} shares of {}", username, quantity, symbol);

        try {
            Transaction transaction = tradingService.buyStock(username, symbol, quantity);
            logger.info("<<< TRADE SUCCESS: Transaction ID {}", transaction.getId());
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            logger.error("<<< TRADE FAILED: Error executing trade for user '{}'", username, e);
            // Return a more informative error to the client
            return ResponseEntity.badRequest().body(null); 
        }
    }

    @PostMapping("/sell/{symbol}")
    public ResponseEntity<?> sellStock(
            @PathVariable String symbol,
            @RequestParam int quantity,
            Authentication authentication) {

                String username = authentication.getName();
                logger.info(">>> TRADE REQUEST: User '{}' wants to SELL {} shares of {}", username, quantity, symbol);

                try {
                    Transaction transaction = tradingService.sellStock(username, symbol, quantity);
                    logger.info("<<< TRADE SUCCESS: Transaction ID {}", transaction.getId());
                    return ResponseEntity.ok(transaction);
                } catch (Exception e) {
                    // TODO: handle exception
                    logger.info("Successfully executed sell transaction: {}", username, e.getMessage());
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }


}