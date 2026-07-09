package com.example.api.port.in;

import com.example.model.Repository;

public interface RepositoryUseCase {
    Repository getRepository(String owner, String repositoryName);
    Repository createRepository(String owner, String repositoryName);
    Repository updateRepository(String owner, String repositoryName);
    void deleteRepository(String owner, String repositoryName);
}
