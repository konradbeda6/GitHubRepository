package com.example.Repositories.model;

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
public class GitHubRepository {
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
}
