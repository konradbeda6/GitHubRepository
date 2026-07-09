package com.example.api.port.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record RepositoryResponseDto(
        String owner,
        @JsonProperty("name")
        String repositoryName,
        @JsonProperty("full_name")
        String fullName,
        String description,
        @JsonProperty("clone_url")
        String cloneUrl,
        @JsonProperty("watchers")
        Long watchers,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
}
