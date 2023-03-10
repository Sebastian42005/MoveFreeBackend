package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class AlreadyCompanyException extends CustomHttpException {
    public AlreadyCompanyException() {
        super("User has already a company", HttpStatus.CONFLICT, false);
    }
}
