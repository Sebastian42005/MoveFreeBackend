package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class NoCompanyException extends Exception implements CustomHttpException {
    public NoCompanyException() {
        super("User has no company");
        log.error("User has no company");
    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(getMessage());
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
