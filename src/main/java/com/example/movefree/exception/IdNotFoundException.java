package com.example.movefree.exception;

import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Slf4j
public class IdNotFoundException extends CustomHttpException {

    public IdNotFoundException(NotFoundType notFoundType) {
        super(notFoundType.getName() + " not found", HttpStatus.NOT_FOUND, false);
    }

    public static Supplier<IdNotFoundException> get(NotFoundType notFoundType) {
        return () -> new IdNotFoundException(notFoundType);
    }
}
