package com.vaistra.payloads;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ErrorResponse {
    String message;
    private HttpStatus status;
    private List<String> errors;

    public ErrorResponse(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }
}
