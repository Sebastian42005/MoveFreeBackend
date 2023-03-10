package com.example.movefree.exception;

import com.example.movefree.exception.superclass.CustomHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class MemberAlreadyHasRoleException extends CustomHttpException {
    public MemberAlreadyHasRoleException() {
        super("Member already has role", HttpStatus.FORBIDDEN, false);
    }
}
