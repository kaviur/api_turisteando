package com.proyecto.turisteando.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public String healthCheck() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return "Database is awake - test!";
        } catch (Exception e) {
            return "Database is not awake: " + e.getMessage();
        }
    }
}
