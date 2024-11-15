package com.proyecto.turisteando.exceptions.customExceptions;

public class ImageLimitExceededException extends RuntimeException {

    public ImageLimitExceededException(String message) {
        super(message);
    }

    public ImageLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
