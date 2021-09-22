package com.example.MyBookShopApp.exception;

public class JwtTokenMalformedException extends Throwable {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
