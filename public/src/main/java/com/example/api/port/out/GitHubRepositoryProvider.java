package com.example.api.port.out;

import com.example.model.Repository;

public interface GitHubRepositoryProvider {
    Repository getRepository(String owner, String repositoryName);
}
