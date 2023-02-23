package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class InvalidInputException extends Exception implements CustomHttpException {

    public InvalidInputException(String message) {
        super(message);
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
    }
}
