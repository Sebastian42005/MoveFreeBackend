package com.example.movefree.exception;

import com.example.movefree.exception.enums.MultipartFileExceptionType;
import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class InvalidMultipartFileException extends CustomHttpException {

    public InvalidMultipartFileException(MultipartFileExceptionType multipartFileExceptionType) {
        super(multipartFileExceptionType.equals(MultipartFileExceptionType.NO_CONTENT)
                        ? "MultipartFile has no content"
                        : "MultipartFile is not an image",
                HttpStatus.BAD_REQUEST,
                false);
    }
}
