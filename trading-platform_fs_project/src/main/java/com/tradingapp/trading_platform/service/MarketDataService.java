package com.tradingapp.trading_platform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class MarketDataService {

    private static final Logger log = LoggerFactory.getLogger(MarketDataService.class);

    @Value("${finnhub.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    // Constructor injection for RestTemplate
    public MarketDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getLivePrice(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            log.warn("Empty or null symbol provided");
            return BigDecimal.ZERO;
        }

        String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol.trim() + "&token=" + apiKey.trim();
        log.info("Fetching live price from: {}", url);

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("c")) {
                Object priceObject = response.get("c");
                if (priceObject instanceof Number || priceObject instanceof String) {
                    BigDecimal price = new BigDecimal(priceObject.toString());
                    log.info("Current price for {} is {}", symbol, price);
                    return price;
                }
            }
            log.warn("Price data not found or invalid for symbol: {}", symbol);
        } catch (Exception e) {
            log.error("Error fetching price for {}: {}", symbol, e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
