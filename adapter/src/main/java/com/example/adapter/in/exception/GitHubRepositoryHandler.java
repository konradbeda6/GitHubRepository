package com.example.adapter.in.exception;

import com.example.model.exception.InternalServerException;
import com.example.model.exception.RepositoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GitHubRepositoryHandler {

    @ExceptionHandler(RepositoryNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRepositoryNotFound(RepositoryNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage(), "NOT_FOUND"));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorMessage> handleInternalServerException(InternalServerException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(exception.getMessage(), "INTERNAL_SERVER_ERROR"));
    }
}
