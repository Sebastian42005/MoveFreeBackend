package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class AlreadySentRequestException extends CustomHttpException {
    public AlreadySentRequestException() {
        super("User has already sent a request", HttpStatus.CONFLICT, false);
    }
}
