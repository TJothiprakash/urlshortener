package com.bitly_clone.in.bitly_clone.exceptionhandling;
public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String message) {
        super(message);
    }
}

