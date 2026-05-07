package com.example.Repositories.exception;

import org.springframework.http.HttpStatus;

public class RepositoryNotFoundException extends GitHubRepositoryException {
    public RepositoryNotFoundException() {
        super("Repository not found", HttpStatus.NOT_FOUND);
    }
}
