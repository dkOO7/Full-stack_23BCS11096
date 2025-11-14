package com.tradingapp.trading_platform.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name= "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private int quantity;

    //  avg buy price
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal avgBuyPrice = BigDecimal.ZERO;
}
