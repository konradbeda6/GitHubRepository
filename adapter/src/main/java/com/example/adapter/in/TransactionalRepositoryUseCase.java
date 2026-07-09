package com.example.adapter.in;

import com.example.api.port.in.RepositoryUseCase;
import com.example.domain.RepositoryService;
import com.example.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionalRepositoryUseCase implements RepositoryUseCase {
    private final RepositoryUseCase repositoryService;

    @Override
    public Repository getRepository(String owner, String repositoryName) {
        return repositoryService.getRepository(owner, repositoryName);
    }

    @Transactional
    @Override
    public Repository createRepository(String owner, String repositoryName) {
        return repositoryService.createRepository(owner, repositoryName);
    }

    @Transactional
    @Override
    public Repository updateRepository(String owner, String repositoryName) {
        return repositoryService.updateRepository(owner, repositoryName);
    }

    @Transactional
    @Override
    public void deleteRepository(String owner, String repositoryName) {
        repositoryService.deleteRepository(owner, repositoryName);
    }
}
