package com.letter2sea.be.exception;

import com.letter2sea.be.exception.response.ErrorResponse;
import com.letter2sea.be.exception.type.CommonExceptionType;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Letter2SeaException.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Letter2SeaException exception) {
        ErrorResponse response = new ErrorResponse(exception);
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ErrorResponse invalidBindHandler(MethodArgumentNotValidException exception) {
        ErrorResponse response = ErrorResponse.builder()
            .errorCode(CommonExceptionType.INVALID_REQUEST.getErrorCode())
            .message(CommonExceptionType.INVALID_REQUEST.getErrorMessage())
            .detail(new HashMap<>())
            .build();

        for (FieldError fieldError : exception.getFieldErrors()) {
            response.addDetail(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }
}
