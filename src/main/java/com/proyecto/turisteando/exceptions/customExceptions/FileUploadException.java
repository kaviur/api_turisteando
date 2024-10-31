package com.proyecto.turisteando.exceptions.customExceptions;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

