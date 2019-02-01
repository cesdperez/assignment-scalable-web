package io.cesdperez.assignmentscalableweb.controller;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {

    private HttpStatus status;

    public ApiException(HttpStatus code, String message) {
        super(message);
        this.status = code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
