package com.knits.enterprise.controller.common;

import com.knits.enterprise.error.ApiError;
import com.knits.enterprise.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiError> userException(UserException e, WebRequest webRequest){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }
}
