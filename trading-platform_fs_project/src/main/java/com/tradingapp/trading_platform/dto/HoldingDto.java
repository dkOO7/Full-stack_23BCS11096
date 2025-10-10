package com.tradingapp.trading_platform.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class HoldingDto {
    private String symbol;
    private int quantity;
    private BigDecimal currentPrice;
    private BigDecimal marketValue;
}