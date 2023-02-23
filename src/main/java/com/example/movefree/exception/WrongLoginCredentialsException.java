package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import org.springframework.http.ResponseEntity;

public class WrongLoginCredentialsException extends Exception implements CustomHttpException {
    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(401).body("Wrong login credentials");
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(401).build();
    }
}
