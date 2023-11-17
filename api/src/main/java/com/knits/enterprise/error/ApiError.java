package com.knits.enterprise.error;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ApiError {
    private String message;
    private Integer errorCode;
}
