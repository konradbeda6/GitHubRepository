package com.example.adapter.in.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class GitHubRepositoryException extends RuntimeException {
    private final HttpStatus status;

    public GitHubRepositoryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
