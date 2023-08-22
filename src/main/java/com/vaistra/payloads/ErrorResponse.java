package com.vaistra.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
@SuperBuilder
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class ErrorResponse {

    private String timeStamp;
    String message;
    private HttpStatus status;
    private List<String> errors;

    public ErrorResponse(String timeStamp,HttpStatus status, String message, List<String> errors) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(String timeStamp, HttpStatus status, String message, String error) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }
}
