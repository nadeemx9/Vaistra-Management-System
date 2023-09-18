package com.vaistra.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.Null;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleGlobalRegularException(MethodArgumentNotValidException ex) {

        Map<String, String> errorMap = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorMap;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        return Map.of("errorMessage", "Request body cannot be empty");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex)
    {
        return Map.of("errorMessage", "Invalid Argument!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex)
    {
        return Map.of("errorMessage", "Invalid Argument!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleResourceNotFound(ResourceNotFoundException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDuplicateEntryException(DuplicateEntryException ex)
    {
        return Map.of("errorMessage", ex.getMessage());

    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InactiveStatusException.class)
    public Map<String, String> handleIsActiveException(InactiveStatusException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserUnauthorizedException.class)
    public Map<String, String> handleUserUnauthorizedException(UserUnauthorizedException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    public Map<String, String> handleIoException(IOException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    // JWT Exception Handling

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public Map<String, String> handleBadCredentialsException(BadCredentialsException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Map<String, String> handleAccessDeniedException(AccessDeniedException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SignatureException.class)
    public Map<String, String> handleSignatureException(SignatureException ex)
    {
        return Map.of("errorMessage", "Invalid JWT Token");
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ExpiredJwtException.class)
    public Map<String, String> handleExpiredJwtException(ExpiredJwtException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(DecodingException.class)
    public Map<String, String> handleDecodingException(DecodingException ex)
    {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map<String, String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex)
    {
        return Map.of("errorMessage", "Maximum upload size is 1MB");
    }
}
