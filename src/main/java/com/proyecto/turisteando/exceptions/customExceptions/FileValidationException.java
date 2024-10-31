package com.proyecto.turisteando.exceptions.customExceptions;

public class FileValidationException extends RuntimeException {
    public FileValidationException(String message) {
        super(message);
    }
}
