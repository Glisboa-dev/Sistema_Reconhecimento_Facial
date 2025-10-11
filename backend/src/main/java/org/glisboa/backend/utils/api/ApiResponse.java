package org.glisboa.backend.utils.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    protected ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static ResponseEntity<ApiResponse> success(String message, Object data, HttpStatus status) {
        return new ResponseEntity<>(new ApiResponse(true, message, data), status);
    }
}
