package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class UserForbiddenException extends Exception implements CustomHttpException {
    public UserForbiddenException() {
        super("User is forbidden");
        log.warn(getMessage());
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
