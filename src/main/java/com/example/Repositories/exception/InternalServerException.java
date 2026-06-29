package com.example.Repositories.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends GitHubRepositoryException {
    public InternalServerException() {
        super("GitHub service unavailable", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
