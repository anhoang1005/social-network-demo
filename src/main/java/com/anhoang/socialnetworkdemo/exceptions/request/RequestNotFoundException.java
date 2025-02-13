package com.anhoang.socialnetworkdemo.exceptions.request;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String s){
        super(s);
    }
}
