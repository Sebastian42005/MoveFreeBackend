package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class WrongLoginCredentialsException extends Exception implements CustomHttpException {

    public WrongLoginCredentialsException() {
        super("Wrong login credentials");
        log.warn(getMessage());
    }
    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(401).body("Wrong login credentials");
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(401).build();
    }
}
