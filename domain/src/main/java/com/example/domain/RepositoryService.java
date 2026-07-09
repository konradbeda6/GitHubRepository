package com.example.domain;

import com.example.api.port.in.RepositoryUseCase;
import com.example.api.port.out.GitHubRepositoryProvider;
import com.example.api.port.out.RepositoryProvider;
import com.example.model.exception.RepositoryNotFoundException;
import com.example.model.Repository;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class RepositoryService implements RepositoryUseCase {
    private final GitHubRepositoryProvider repositoryClient;
    private final RepositoryProvider jpaRepository;

    public Repository getRepository(String owner, String repositoryName) {
        return jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new);
    }

    public Repository createRepository(String owner, String repositoryName) {
        Repository response = repositoryClient.getRepository(owner, repositoryName);
        System.out.println("cloneUrl = " + response.getCloneUrl());
        return jpaRepository.save(response);
    }

    public Repository updateRepository(String owner, String repositoryName) {
        CompletableFuture<Repository> repositoryFuture = CompletableFuture.supplyAsync(() -> jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new));
        CompletableFuture<Repository> responseFuture = CompletableFuture.supplyAsync(() -> repositoryClient.getRepository(owner, repositoryName));

        Repository repository = repositoryFuture.join();
        Repository response = responseFuture.join();
        repository.update(response);
        return jpaRepository.save(repository);
    }

    public void deleteRepository(String owner, String repositoryName) {
        CompletableFuture<Repository> repositoryFuture = CompletableFuture.supplyAsync(() -> jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new));

        Repository repository = repositoryFuture.join();
        jpaRepository.deleteByOwnerAndRepositoryName(owner, repositoryName);
    }
}
