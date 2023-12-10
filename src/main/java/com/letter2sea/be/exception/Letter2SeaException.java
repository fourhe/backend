package com.letter2sea.be.exception;

import com.letter2sea.be.exception.type.ExceptionType;
import org.springframework.http.HttpStatus;

public class Letter2SeaException extends RuntimeException {

    private final ExceptionType exceptionType;

    public Letter2SeaException(ExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }

    public HttpStatus getStatus() {
        return exceptionType.getStatus();
    }
}
