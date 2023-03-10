package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class InvalidInputException extends CustomHttpException {

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST, false);
    }
}
