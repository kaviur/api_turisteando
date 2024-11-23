package com.proyecto.turisteando.exceptions.customExceptions;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(String message) { super(message); }
}
