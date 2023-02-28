package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class AlreadyCompanyException extends Exception implements CustomHttpException {
    public AlreadyCompanyException() {
        super("User is already a company");
        log.warn(getMessage());
    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(getMessage());
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
