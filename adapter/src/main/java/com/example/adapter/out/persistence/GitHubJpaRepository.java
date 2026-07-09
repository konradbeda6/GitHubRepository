package com.example.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitHubJpaRepository extends JpaRepository<GitHubRepositoryEntity, Long> {
    Optional<GitHubRepositoryEntity> findByOwnerAndRepositoryName(String owner, String repositoryName);

    void deleteByOwnerAndRepositoryName(String owner, String repositoryName);

}
