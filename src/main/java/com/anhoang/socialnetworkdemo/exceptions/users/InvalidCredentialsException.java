package com.anhoang.socialnetworkdemo.exceptions.users;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
