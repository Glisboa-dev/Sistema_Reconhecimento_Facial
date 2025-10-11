package org.glisboa.backend.exception.auth;

import org.glisboa.backend.exception.api.ApiException;
import org.springframework.http.HttpStatus;

public class AuthException extends ApiException {
    public AuthException(String message) {
        super("Erro de autenticação",message, HttpStatus.UNAUTHORIZED);
    }
}
