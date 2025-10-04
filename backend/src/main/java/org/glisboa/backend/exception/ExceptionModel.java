package org.glisboa.backend.exception;

import org.springframework.http.HttpStatus;

public class ExceptionModel extends RuntimeException {
    public ExceptionModel(HttpStatus status, String message) {
        super(message);
    }
}
