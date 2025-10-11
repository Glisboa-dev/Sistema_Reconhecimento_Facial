package org.glisboa.backend.exception.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public abstract class ApiException extends RuntimeException{
    private final String error;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    protected ApiException(String error, String message, HttpStatus status) {
        super(message);
        this.error = error;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
