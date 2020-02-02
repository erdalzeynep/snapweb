package com.detectify.screenshot.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {
        super("No record found with given Ids");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
