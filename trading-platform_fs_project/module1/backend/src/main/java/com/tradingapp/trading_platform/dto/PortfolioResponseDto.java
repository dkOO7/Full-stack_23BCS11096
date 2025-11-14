package com.tradingapp.trading_platform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponseDto {
    private BigDecimal cashBalance;
    private BigDecimal totalStockValue;
    private BigDecimal totalPortfolioValue;
    private List<HoldingDto> holdings;
}
