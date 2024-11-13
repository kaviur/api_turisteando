package com.proyecto.turisteando.exceptions.customExceptions;

public class CharacteristicNotFoundException extends RuntimeException {
    public CharacteristicNotFoundException(String message) {
        super(message);
    }
}
