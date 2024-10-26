package com.proyecto.turisteando.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response {
    private Boolean success;
    private List<String> errors = new ArrayList<>();
    private HttpStatus httpStatus;
    private Object data;
    private String debugMessage; // only for development environment

    /**
     * Constructor for production environment in case of success without data
     * @param success
     * @param httpStatus
     */
    public Response(Boolean success, HttpStatus httpStatus) {
        this.success = success;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructor for production environment in case of success with data
     * @param success
     * @param httpStatus
     * @param data
     */
    public Response(Boolean success, HttpStatus httpStatus, Object data) {
        this.success = success;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    /**
     * Constructor for production environment in case of errors
     *
     * @param success
     * @param httpStatus
     * @param errors
     */
    public Response(Boolean success, HttpStatus httpStatus, List<String> errors) {
        this.success = success;
        this.errors = errors;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructor for development environment in case of errors
     *
     * @param success
     * @param httpStatus
     * @param errors
     * @param debugMessage
     */
    public Response(Boolean success, HttpStatus httpStatus, List<String> errors, String debugMessage) {
        this.success = success;
        this.errors = errors;
        this.httpStatus = httpStatus;
        this.debugMessage = debugMessage;
    }
}