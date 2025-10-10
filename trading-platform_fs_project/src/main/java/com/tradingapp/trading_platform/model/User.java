package com.tradingapp.trading_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    
    @Id  //JPA marks this as primary key 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB: auto increment the id
    private long id;

    @Column(nullable = false , unique=true)
    private String username;

    @Column (nullable= false, unique = true)
    private String email;

    @Column (nullable = false)
    private String password;
}
