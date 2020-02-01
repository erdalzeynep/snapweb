package com.detectify.screenshot.exception;

public class EmptyUrlListException extends RuntimeException {

    public EmptyUrlListException() {
        super("You are not allowed to give an empty URL list");
    }
}
