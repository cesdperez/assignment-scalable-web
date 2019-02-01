package io.cesdperez.assignmentscalableweb.controller;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.cesdperez.assignmentscalableweb.dto.ApiError;

@Order(HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiErrorHandler.class);

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("The API returned an error with status code {} while processing a request", BAD_REQUEST, ex);
        ApiError error = new ApiError(BAD_REQUEST, "Request body is invalid", ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException ex) {
        log.info("The API returned an error with status code {} while processing a request", ex.getStatus(), ex);
        ApiError error = new ApiError(ex);
        return buildResponseEntity(error);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(error);
    }
}
