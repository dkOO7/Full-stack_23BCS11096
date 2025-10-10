package com.tradingapp.trading_platform.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="portfolios")
public class Portfolio {
    
    @Id
    private long id; // same id as user 1-1 link 
    
    @OneToOne
    @MapsId   // this links portfolio id to user id
    @JoinColumn(name ="id")
    private User user;

    @Column(nullable = false , precision = 10, scale=4 )  // for accuracy
    private BigDecimal cashBalance;

}
