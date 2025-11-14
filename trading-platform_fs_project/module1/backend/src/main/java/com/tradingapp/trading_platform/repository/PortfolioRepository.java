package com.tradingapp.trading_platform.repository;

import com.tradingapp.trading_platform.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // option 1 — Let Spring derive query automatically (works if mapping names are correct)
    Optional<Portfolio> findByUser_Username(String username);

    //  Option 2 — Let Spring derive query automatically (works if mapping names are correct)
    @Query("SELECT p FROM Portfolio p WHERE p.user.username = :username")
    Optional<Portfolio> findByUsername(@NonNull @Param("username") String username);


}
