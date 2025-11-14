// // package com.tradingapp.trading_platform.controller;

// import com.tradingapp.trading_platform.model.Transaction;
// import com.tradingapp.trading_platform.repository.TransactionRepository;
// import com.tradingapp.trading_platform.service.TradingService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.math.BigDecimal;
// import java.util.List;

// @RestController
// @RequestMapping("/api/trades")
// public class TradeHistoryController {

//     private final TradingService tradingService;
//     private final TransactionRepository transactionRepository;

//     @Autowired
//     public TradeHistoryController(TradingService tradingService,
//                                   TransactionRepository transactionRepository) {
//         this.tradingService = tradingService;
//         this.transactionRepository = transactionRepository;
//     }

//     // ✅ 1️⃣ Fetch trade history for logged-in user (JSON)
//     @GetMapping
//     public ResponseEntity<List<Transaction>> getUserTrades(Authentication authentication) {
//         String username = authentication.getName();
//         List<Transaction> trades = tradingService.getTradeHistory(username);
//         return ResponseEntity.ok(trades);
//     }

//     // ✅ 2️⃣ Export trade history as CSV
//     @GetMapping(value = "/export", produces = "text/csv")
//     public void exportTradeHistory(HttpServletResponse response, Authentication auth) throws IOException {
//         String username = auth.getName();

//         // Fetch all trades for this user's portfolio, ordered by date descending
//         List<Transaction> trades =
//                 transactionRepository.findByPortfolio_User_UsernameOrderByTimestampDesc(username);

//         // Set response headers
//         response.setContentType("text/csv");
//         response.setHeader("Content-Disposition", "attachment; filename=\"trade_history.csv\"");

//         // Write CSV
//         PrintWriter writer = response.getWriter();
//         writer.println("Symbol,Type,Quantity,PricePerShare,TotalValue,Date");

//         for (Transaction t : trades) {
//             BigDecimal totalValue = t.getPricePerShare().multiply(BigDecimal.valueOf(t.getQuantity()));
//             writer.printf("%s,%s,%d,%.2f,%.2f,%s%n",
//                     t.getStock().getSymbol(),
//                     t.getType(),
//                     t.getQuantity(),
//                     t.getPricePerShare(),
//                     totalValue,
//                     t.getTimestamp());
//         }

//         writer.flush();
//         writer.close();
//     }
// }
