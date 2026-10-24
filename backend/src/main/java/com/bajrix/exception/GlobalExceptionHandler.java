package com.bajrix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleForbidden(SecurityException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("FORBIDDEN", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("INTERNAL_ERROR", "Unexpected error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // simple error payload
    static class ErrorResponse {
        private String code;
        private String message;
        public ErrorResponse(String code, String message) {this.code = code; this.message = message;}
        public String getCode() {return code;}
        public String getMessage() {return message;}
    }
}
