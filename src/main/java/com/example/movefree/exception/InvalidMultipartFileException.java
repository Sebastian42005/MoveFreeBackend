package com.example.movefree.exception;

import com.example.movefree.exception.enums.MultipartFileExceptionType;
import com.example.movefree.exception.interfaces.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class InvalidMultipartFileException extends Exception implements CustomHttpException {

    public InvalidMultipartFileException(MultipartFileExceptionType multipartFileExceptionType) {
        super(multipartFileExceptionType.equals(MultipartFileExceptionType.NO_CONTENT) ? "MultipartFile has no content" : "MultipartFile is not an image");
        log.warn(getMessage());
    }

    @Override
    public ResponseEntity<String> getResponseEntityWithMessage() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getMessage());
    }

    @Override
    public <T> ResponseEntity<T> getResponseEntity() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
