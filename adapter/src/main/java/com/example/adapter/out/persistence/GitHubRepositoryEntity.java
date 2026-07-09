package com.example.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "repositories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GitHubRepositoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String owner;
    @Column(nullable = false)
    private String repositoryName;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String cloneUrl;
    @Column(nullable = false)
    private Long watchers;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void update(GitHubResponseDto responseDto) {
        if (responseDto.owner() != null) {
            this.owner = responseDto.owner().login();
        }
        if (responseDto.repositoryName() != null) {
            this.repositoryName = responseDto.repositoryName();
        }
        if (responseDto.fullName() != null) {
            this.fullName = responseDto.fullName();
        }
        if (responseDto.description() != null) {
            this.description = responseDto.description();
        }
        if (responseDto.cloneUrl() != null) {
            this.cloneUrl = responseDto.cloneUrl();
        }
        if (responseDto.watchers() != null) {
            this.watchers = responseDto.watchers();
        }
        if (responseDto.createdAt() != null) {
            this.createdAt = responseDto.createdAt();
        }
    }
}