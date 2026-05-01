package com.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Reuses the common library's exception-to-response mapping for this service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends com.commonlib.exception.GlobalExceptionHandler {

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
//        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return new ResponseEntity<>(response, ex.getStatus());
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex){
//        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return new ResponseEntity<>(response, ex.getStatus());
//    }
//
//    @ExceptionHandler(MissingParameterException.class)
//    public ResponseEntity<ErrorResponse> handleMissingParameterException(MissingParameterException ex){
//        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return new ResponseEntity<>(response, ex.getStatus());
//    }
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
//    }
}
