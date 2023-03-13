package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import org.springframework.http.HttpStatus;

public class CompanyAlreadyExistsException extends CustomHttpException {

    public CompanyAlreadyExistsException() {
        super("Company already exists", HttpStatus.CONFLICT, false);
    }
}
