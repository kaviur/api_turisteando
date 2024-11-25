package com.proyecto.turisteando.exceptions.customExceptions;

public class ReviewWithoutReservationException extends RuntimeException{
    public ReviewWithoutReservationException(String message) {
        super(message);
    }
}
