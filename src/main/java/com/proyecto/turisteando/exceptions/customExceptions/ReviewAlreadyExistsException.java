package com.proyecto.turisteando.exceptions.customExceptions;

public class ReviewAlreadyExistsException extends RuntimeException{
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
