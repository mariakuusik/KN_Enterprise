package com.knits.enterprise.error;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ApiError {

    private int errorCode;
    private String message;
    private Date timestamp;

    public ApiError(int errorCode, String message, Date timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }
}
