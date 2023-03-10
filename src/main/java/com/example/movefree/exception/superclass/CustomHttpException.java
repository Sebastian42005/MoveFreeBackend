package com.example.movefree.exception.superclass;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class CustomHttpException extends Exception{
    private final HttpStatus httpStatus;

    public CustomHttpException(String message, HttpStatus httpStatus, boolean error) {
        super(message);
        this.httpStatus = httpStatus;
        if (error) {
            log.error(getMessage());
        } else {
            log.warn(getMessage());
        }
    }

    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(httpStatus).build();
    }

    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(httpStatus).body(getMessage());
    }
}
