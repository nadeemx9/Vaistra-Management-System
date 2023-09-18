package com.vaistra.exception;

public class UserUnauthorizedException extends RuntimeException{

    public UserUnauthorizedException(String msg)
    {
        super(msg);
    }
}
