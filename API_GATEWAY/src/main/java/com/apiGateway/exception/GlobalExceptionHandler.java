package com.apiGateway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.apiGateway.exception.ErrorResponse;
import com.apiGateway.exception.BadRequestException;

/**
 * Converts gateway validation failures into a consistent JSON error payload.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex){

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus());
        // Gateway errors are returned directly because there is no downstream service to delegate to.
        return new ResponseEntity<>(response, ex.getStatus());
    }
}
