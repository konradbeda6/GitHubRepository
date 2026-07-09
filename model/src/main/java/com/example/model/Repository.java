package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Repository {

    private String owner;
    private String repositoryName;
    private String fullName;
    private String description;
    private String cloneUrl;
    private Long watchers;
    private LocalDateTime createdAt;

    public void update(Repository repository) {
        if (repository.getOwner() != null) {
            this.owner = repository.getOwner();
        }
        if (repository.getRepositoryName() != null) {
            this.repositoryName = repository.getRepositoryName();
        }
        if (repository.getFullName() != null) {
            this.fullName = repository.getFullName();
        }
        if (repository.getDescription() != null) {
            this.description = repository.getDescription();
        }
        if (repository.getCloneUrl() != null) {
            this.cloneUrl = repository.getCloneUrl();
        }
        if (repository.getWatchers() != null) {
            this.watchers = repository.getWatchers();
        }
        if (repository.getCreatedAt() != null) {
            this.createdAt = repository.getCreatedAt();
        }
    }
}
