package com.proyecto.turisteando.handlers;

import com.proyecto.turisteando.exceptions.customExceptions.*;
import com.proyecto.turisteando.utils.Response;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the API in development environment.
 */
@Slf4j
@ControllerAdvice
@Profile("dev")
public class GlobalExceptionHandler {

    private static final String GENERIC_DATABASE_ERROR = "Ocurrió un error de base de datos, intenta más tarde.";
    private static final String RESOURCE_NOT_FOUND = "El recurso solicitado no se encontró: ";
    private static final String AUTHENTICATION_ERROR = "Error de autenticación. Verifica tus credenciales.";
    private static final String INTERNAL_ERROR = "Error al procesar la solicitud. Intenta más tarde o contacta soporte.";
    private static final String VALIDATION_ERROR = "Verifica los campos ingresados.";

    @ExceptionHandler({
            CategoryNotFoundException.class,
            NoResourceFoundException.class,
            EntityNotFoundException.class,
            TouristPlanNotFoundException.class,
            CityNotFoundException.class,
            CountryNotFoundException.class,
            ImageNotFoundException.class,
            ReservationNotFoundException.class
    })

    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        return buildErrorResponse(List.of(RESOURCE_NOT_FOUND + ex.getMessage()), ex, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler({
//            LockedException.class,
//            DisabledException.class,
//            BadCredentialsException.class,
//            UnauthorizedException.class
//    })
//    public ResponseEntity<Object> handleAuthenticationExceptions(Exception ex) {
//        log.warn("Error de autenticación: {}", ex.getMessage());
//        return buildErrorResponse(List.of(AUTHENTICATION_ERROR), ex, HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return buildErrorResponse(errorMessages, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDatabaseException(DataIntegrityViolationException ex) {
        String message = GENERIC_DATABASE_ERROR;
        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof SQLIntegrityConstraintViolationException) {
            message = "Violación de integridad de datos. Verifica los valores ingresados.";
        } else if (rootCause instanceof QueryTimeoutException) {
            message = "Tiempo de espera agotado. Intenta más tarde.";
        }

        log.error("Error en base de datos: {}", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        return buildErrorResponse(List.of(message), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildErrorResponse(List.of(INTERNAL_ERROR), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErrorResponse(List<String> errorMessages, Throwable ex, HttpStatus status) {
        String debugMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        Response response = new Response(false, status, errorMessages, debugMessage);
        return new ResponseEntity<>(response, status);
    }
}