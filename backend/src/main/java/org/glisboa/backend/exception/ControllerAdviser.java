package org.glisboa.backend.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.glisboa.backend.exception.api.ApiException;
import org.glisboa.backend.exception.auth.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdviser {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex){
        return buildResponse("Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex){
        return buildResponse("Not Found", ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex){
        return buildResponse(ex.getError(), ex.getMessage(), ex.getStatus(), ex.getTimestamp());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        return buildResponse("Validation Error", message, HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        return buildResponse("Not Found", ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> handleEntityExists(EntityExistsException ex) {
        return buildResponse("Conflict", ex.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        return buildResponse("Unauthorized", ex.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
    }

    private ResponseEntity<Object> buildResponse(String error, String message, HttpStatus status, LocalDateTime timestamp) {
        Map<String, Object> response = new HashMap<>();

        response.put("error", error);
        response.put("message", message);
        response.put("status", status.value());
        response.put("timestamp", timestamp);

        return new ResponseEntity<>(response, status);
    }
}
