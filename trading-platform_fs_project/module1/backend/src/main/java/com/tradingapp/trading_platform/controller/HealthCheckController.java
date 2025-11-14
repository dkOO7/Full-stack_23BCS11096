package com.tradingapp.trading_platform.controller; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {

    /**
     * Responds to GET requests at "/ping".
     * @return A simple string "Pong!" to indicate the service is alive.
     */
    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }
}