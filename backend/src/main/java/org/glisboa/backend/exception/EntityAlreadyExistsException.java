package org.glisboa.backend.exception;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends ExceptionModel{
    public EntityAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
