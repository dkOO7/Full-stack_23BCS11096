package com.tradingapp.trading_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String symbol;  // AAPL, google

    @Column(nullable = false)
    private String name;
}
