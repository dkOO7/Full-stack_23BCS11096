package com.tradingapp.trading_platform.repository;

import com.tradingapp.trading_platform.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    Optional<Holding> findByPortfolioIdAndStockId(Long portfolioId, Long stockId);
    List<Holding> findByPortfolioId(Long portfolioId);
}