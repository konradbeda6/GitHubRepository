package com.example.api.port.out;

import com.example.model.Repository;

import java.util.Optional;

public interface RepositoryProvider {
    Optional<Repository> findByOwnerAndRepositoryName(String owner, String repositoryName);
    Repository save(Repository repository);
    void deleteByOwnerAndRepositoryName(String owner, String repositoryName);
}
