package io.cesdperez.assignmentscalableweb.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import io.cesdperez.assignmentscalableweb.controller.ApiException;
import lombok.Getter;

@Getter
public class ApiError {

    private final Instant timestamp = Instant.now();
    private final HttpStatus status;
    private final String message;
    private final String debugMessage;

    public ApiError(ApiException exception) {
        this.status = exception.getStatus();
        this.message = exception.getMessage();
        this.debugMessage = null;
    }

    public ApiError(HttpStatus status, String message, Throwable exception) {
        this.status = status;
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }
}
