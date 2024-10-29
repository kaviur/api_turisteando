package com.proyecto.turisteando.exceptions.customExceptions;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String message) {
        super(message);
    }
}
