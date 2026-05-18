package com.example.Repositories.model;

import java.time.LocalDateTime;

public record GitHubRepositoryDto(
        Long id,
        String fullName,
        String description,
        String cloneUrl,
        Long watchers,
        LocalDateTime createdAt
) {
}
