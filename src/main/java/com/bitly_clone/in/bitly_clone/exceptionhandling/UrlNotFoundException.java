package com.bitly_clone.in.bitly_clone.exceptionhandling;
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String message) {
        super(message);
    }
}
