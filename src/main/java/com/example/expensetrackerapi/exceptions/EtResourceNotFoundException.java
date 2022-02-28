package com.example.expensetrackerapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EtResourceNotFoundException extends RuntimeException {

    public EtResourceNotFoundException(String message) {
        super(message);
    }
}
