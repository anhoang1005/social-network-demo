package com.anhoang.socialnetworkdemo.exceptions.users;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String message) {
        super(message);
    }
}
