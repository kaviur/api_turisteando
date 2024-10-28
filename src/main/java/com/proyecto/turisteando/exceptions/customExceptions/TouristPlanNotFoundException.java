package com.proyecto.turisteando.exceptions.customExceptions;

public class TouristPlanNotFoundException extends RuntimeException {

    public TouristPlanNotFoundException(String message) {
        super(message);
    }

}
