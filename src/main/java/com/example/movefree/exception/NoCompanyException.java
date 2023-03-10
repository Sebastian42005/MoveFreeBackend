package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class NoCompanyException extends CustomHttpException {
    public NoCompanyException() {
        super("User has no company", HttpStatus.FORBIDDEN, true);
    }
}
