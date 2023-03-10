package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomHttpException {

    public UserAlreadyExistsException() {
        super("User already exists", HttpStatus.CONFLICT, false);
    }
}
