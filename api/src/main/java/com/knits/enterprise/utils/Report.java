package com.knits.enterprise.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class Report<T> {
    private Long entityId;
    private int code;
    private String message;


    public Report(Long entityId, int code, String message) {
        this.entityId = entityId;
        this.code = code;
        this.message = message;
    }
}
