package com.example.movefree.exception;

import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class PictureOverflowException extends Exception implements CustomHttpException {
    public PictureOverflowException() {
        super("Too many pictures uploaded");
        log.warn(getMessage());
    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(getMessage());
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
    }
}
