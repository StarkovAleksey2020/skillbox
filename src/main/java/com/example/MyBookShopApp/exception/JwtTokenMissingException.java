package com.example.MyBookShopApp.exception;

public class JwtTokenMissingException extends Throwable {
    public JwtTokenMissingException(String message) {
        super(message);
    }
}
