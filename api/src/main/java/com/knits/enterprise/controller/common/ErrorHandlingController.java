package com.knits.enterprise.controller.common;

import com.knits.enterprise.error.ApiError;
import com.knits.enterprise.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.transaction.SystemException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiError> handleUserException(UserException e) {
        LocalDateTime dateTime = LocalDateTime.now();
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                dateTime
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiError> handleSystemException(SystemException e) {
        LocalDateTime dateTime = LocalDateTime.now();
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                dateTime
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(IOException e) {
        LocalDateTime dateTime = LocalDateTime.now();
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An error occurred while processing the request",
                dateTime
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleRequestBodyValidationException(MethodArgumentNotValidException e) {
        LocalDateTime dateTime = LocalDateTime.now();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        e.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        Map<String, Object> validationErrorResponse = new HashMap<>();
        validationErrorResponse.put("timeStamp", dateTime);
        validationErrorResponse.put("status", status.value());
        validationErrorResponse.put("errors", errors);

        return new ResponseEntity<>(validationErrorResponse, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleRequestParamValidationException(ConstraintViolationException e) {
        LocalDateTime dateTime = LocalDateTime.now();
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        List<String> errors = new ArrayList<>();

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : constraintViolations) {
            errors.add(violation.getMessage());
        }

        Map<String, Object> validationErrorResponse = new HashMap<>();
        validationErrorResponse.put("timeStamp", dateTime);
        validationErrorResponse.put("status", status.value());
        validationErrorResponse.put("error", errors);

        return new ResponseEntity<>(validationErrorResponse, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleInvalidParameterTypeException(MethodArgumentTypeMismatchException e) {
        LocalDateTime dateTime = LocalDateTime.now();
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid parameter type",
                dateTime
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
