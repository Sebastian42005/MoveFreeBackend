package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserAlreadyExistsException extends Exception implements CustomHttpException {

    public UserAlreadyExistsException() {
        super("User already exists");

    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
