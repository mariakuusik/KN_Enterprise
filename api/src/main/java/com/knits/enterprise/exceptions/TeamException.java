package com.knits.enterprise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TeamException extends AppException {


    public TeamException(String message){
        super(message);
    }
}
