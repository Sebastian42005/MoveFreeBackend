package com.example.movefree.exception.interfaces;

import org.springframework.http.ResponseEntity;

public interface CustomHttpException {
    ResponseEntity<String> getResponseEntityWithMessage();
    <T> ResponseEntity<T> getResponseEntity();
}

