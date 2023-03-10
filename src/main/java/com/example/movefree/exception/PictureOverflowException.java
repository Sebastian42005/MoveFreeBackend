package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class PictureOverflowException extends CustomHttpException {
    public PictureOverflowException() {
        super("Picture overflow", HttpStatus.PAYLOAD_TOO_LARGE, false);
    }
}
