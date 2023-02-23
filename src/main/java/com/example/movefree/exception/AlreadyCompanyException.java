package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AlreadyCompanyException extends Exception implements CustomHttpException {
    public AlreadyCompanyException() {
        super("User is already a company");
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
