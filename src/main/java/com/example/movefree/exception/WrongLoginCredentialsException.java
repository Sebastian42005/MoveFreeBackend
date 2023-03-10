package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class WrongLoginCredentialsException extends CustomHttpException {

    public WrongLoginCredentialsException() {
        super("Wrong login credentials", HttpStatus.UNAUTHORIZED, false);
    }
}
