package com.tradingapp.trading_platform.service;

import com.tradingapp.trading_platform.model.*;
import com.tradingapp.trading_platform.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TradingService {

    private static final Logger logger = LoggerFactory.getLogger(TradingService.class);

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;
    private final MarketDataService marketDataService;

    @Autowired
    public TradingService(UserRepository userRepository,
                          PortfolioRepository portfolioRepository,
                          StockRepository stockRepository,
                          HoldingRepository holdingRepository,
                          TransactionRepository transactionRepository,
                          MarketDataService marketDataService) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.holdingRepository = holdingRepository;
        this.transactionRepository = transactionRepository;
        this.marketDataService = marketDataService;
    }

    @Transactional
    public Transaction buyStock(String username, String symbol, int quantity) {
        logger.info("Attempting to buy {} shares of {} for user {}", quantity, symbol, username);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }

        // 1. Find user and portfolio
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Portfolio portfolio = portfolioRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        logger.info("Found portfolio with ID: {}", portfolio.getId());

        // 2. Get live price
        double livePriceDouble = marketDataService.getLivePrice(symbol.trim()).doubleValue();
        logger.info("Fetched live price for {}: {}", symbol, livePriceDouble);
        if (livePriceDouble == 0.0) {
            throw new RuntimeException("Could not fetch price for symbol: " + symbol);
        }
        BigDecimal pricePerShare = BigDecimal.valueOf(livePriceDouble);
        BigDecimal totalCost = pricePerShare.multiply(BigDecimal.valueOf(quantity));
        logger.info("Total cost of transaction: {}", totalCost);

        // 3. Check funds
        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            logger.warn("Insufficient funds. User has {}, but needs {}", portfolio.getCashBalance(), totalCost);
            throw new RuntimeException("Insufficient funds to complete purchase.");
        }
        logger.info("User has sufficient funds.");

        // 4. Debit cash
        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalCost));
        portfolioRepository.save(portfolio);
        logger.info("Debited cash. New balance: {}", portfolio.getCashBalance());

        // 5. Find or create stock
        Stock stock = stockRepository.findBySymbol(symbol.trim())
                .orElseGet(() -> {
                    logger.info("Stock {} not found in DB, creating new entry.", symbol);
                    Stock newStock = new Stock();
                    newStock.setSymbol(symbol.trim());
                    newStock.setName(symbol.trim());  // You may want a better name here
                    return stockRepository.save(newStock);
                });
        logger.info("Using stock with ID: {}", stock.getId());

        // 6. Update holding
        Holding holding = holdingRepository.findByPortfolioIdAndStockId(portfolio.getId(), stock.getId())
                .orElseGet(() -> {
                    logger.info("No existing holding for stock ID {}, creating new holding.", stock.getId());
                    Holding newHolding = new Holding();
                    newHolding.setPortfolio(portfolio);
                    newHolding.setStock(stock);
                    newHolding.setQuantity(0);
                    return newHolding;
                });
        holding.setQuantity(holding.getQuantity() + quantity);
        holdingRepository.save(holding);
        logger.info("Updated holding. New quantity for {}: {}", symbol, holding.getQuantity());

        // 7. Create transaction record
        Transaction transaction = new Transaction();
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setType(TransactionType.BUY);
        transaction.setQuantity(quantity);
        transaction.setPricePerShare(pricePerShare);
        transaction.setTimestamp(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("Created transaction record with ID: {}", savedTransaction.getId());

        return savedTransaction;
    }

    @Transactional
    public Transaction sellStock(String username, String symbol, int quantityToSell) {
        logger.info("Attempting to sell {} shares of {} for user {}", quantityToSell, symbol, username);

        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("Quantity to sell must be positive");
        }
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }

        // Find the user, portfolio, and stock
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Portfolio portfolio = portfolioRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        Stock stock = stockRepository.findBySymbol(symbol.trim())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        logger.info("Found portfolio ID {} and stock ID {}", portfolio.getId(), stock.getId());

        // Find the specific holding and check quantity
        Holding holding = holdingRepository.findByPortfolioIdAndStockId(portfolio.getId(), stock.getId())
                .orElseThrow(() -> new RuntimeException("Holding not found"));

        if (holding.getQuantity() < quantityToSell) {
            logger.warn("Insufficient shares. User has {}, but wants to sell {}", holding.getQuantity(), quantityToSell);
            throw new RuntimeException("Not enough shares to sell");
        }
        logger.info("User has enough shares to sell");

        // Get live price and calculate total value
        
        double livePriceDouble = marketDataService.getLivePrice(symbol.trim()).doubleValue();
        logger.info("Fetched live price for {}: {}", symbol, livePriceDouble);
        if (livePriceDouble == 0.0) {
            throw new RuntimeException("Could not fetch price for symbol: " + symbol);
        }
        BigDecimal pricePerShare = BigDecimal.valueOf(livePriceDouble);
        BigDecimal totalValue = pricePerShare.multiply(BigDecimal.valueOf(quantityToSell));
        logger.info("Total value of transaction: {}", totalValue);

        // Decrease holding quantity
        holding.setQuantity(holding.getQuantity() - quantityToSell);
        holdingRepository.save(holding);
        logger.info("Updated holding. New quantity for {}: {}", symbol, holding.getQuantity());

        // Credit cash to portfolio
        portfolio.setCashBalance(portfolio.getCashBalance().add(totalValue));
        portfolioRepository.save(portfolio);
        logger.info("Credited cash. New balance: {}", portfolio.getCashBalance());

        // Create a transaction record
        Transaction transaction = new Transaction();
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setType(TransactionType.SELL);
        transaction.setQuantity(quantityToSell);
        transaction.setPricePerShare(pricePerShare);
        transaction.setTimestamp(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("Created transaction record with ID: {}", savedTransaction.getId());

        return savedTransaction;
    }
}
