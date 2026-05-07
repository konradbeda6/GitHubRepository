package com.example.Repositories;

import java.time.LocalDateTime;

public record RepositoryDto(
        String fullName,
        String description,
        String cloneUrl,
        Long stars,
        LocalDateTime createdAt
) {
    public static RepositoryDto of(GitHubResponseDto gitHubResponseDto) {
        return new RepositoryDto(gitHubResponseDto.full_name(), gitHubResponseDto.description(), gitHubResponseDto.clone_url(), gitHubResponseDto.watchers(), gitHubResponseDto.created_at());
    }
}
