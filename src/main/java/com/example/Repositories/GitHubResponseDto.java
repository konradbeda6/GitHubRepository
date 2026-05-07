package com.example.Repositories;

import java.time.LocalDateTime;

public record GitHubResponseDto(
        String full_name,
        String description,
        String clone_url,
        Long watchers,
        LocalDateTime created_at
) {
}
