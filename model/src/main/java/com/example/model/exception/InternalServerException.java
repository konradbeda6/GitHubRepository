package com.example.model.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super("GitHub service unavailable");
    }
}
