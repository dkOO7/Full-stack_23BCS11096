package com.tradingapp.trading_platform.repository;

import com.tradingapp.trading_platform.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}