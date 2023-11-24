package com.knits.enterprise.controller.common;

import com.knits.enterprise.error.ApiError;
import com.knits.enterprise.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiError> userException(UserException e){
        LocalDateTime dateTime = LocalDateTime.now();
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                dateTime
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
