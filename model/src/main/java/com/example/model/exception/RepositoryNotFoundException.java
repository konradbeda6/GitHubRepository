package com.example.model.exception;

public class RepositoryNotFoundException extends RuntimeException {
    public RepositoryNotFoundException() {
        super("Repository not found");
    }
}
