package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AlreadySentRequestException extends Exception implements CustomHttpException {
    public AlreadySentRequestException() {
        super("User has already sent a request");
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
