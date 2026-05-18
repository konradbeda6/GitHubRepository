package com.example.Repositories.repository;

import com.example.Repositories.model.GitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitHubJpaRepository extends JpaRepository<GitHubRepository, Long> {
    Optional<GitHubRepository> findByOwnerAndRepositoryName(String owner, String repositoryName);
}
