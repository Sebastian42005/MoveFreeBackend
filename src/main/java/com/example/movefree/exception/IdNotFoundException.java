package com.example.movefree.exception;

import com.example.movefree.exception.enums.enums.NotFoundType;
import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

@Slf4j
public class IdNotFoundException extends Exception implements CustomHttpException {

    public IdNotFoundException(NotFoundType notFoundType) {
        super(notFoundType.getName() + " not found");
        log.warn(getMessage());
    }

    public static Supplier<IdNotFoundException> get(NotFoundType notFoundType) {
        return () -> new IdNotFoundException(notFoundType);
    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage());
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
