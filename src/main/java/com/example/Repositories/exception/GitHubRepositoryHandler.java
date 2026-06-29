package com.example.Repositories.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GitHubRepositoryHandler {

    @ExceptionHandler(GitHubRepositoryException.class)
    public ResponseEntity<ErrorMessage> handleGitHubRepositoryException(GitHubRepositoryException exception) {
        log.warn("Handled business exception: type={}, message={}, status={}",
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                exception.getStatus());

        return ResponseEntity.status(exception.getStatus()).body(new ErrorMessage(exception.getMessage(), exception.getStatus().toString()));
    }
}
