package com.tradingapp.trading_platform.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    private Long id; // same as user ID (1:1 link)

    //  Correct 1:1 mapping to User
    @OneToOne
    @MapsId // tells JPA to use the same ID as the user
    @JoinColumn(name = "id") // foreign key column in "portfolios" table
    private User user;

    // Cash balance (example: 100000.0000)
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal cashBalance = BigDecimal.ZERO;

    //  Optional: You can track portfolio value
    @Column(precision = 19, scale = 4)
    private BigDecimal totalPortfolioValue = BigDecimal.ZERO;
}
