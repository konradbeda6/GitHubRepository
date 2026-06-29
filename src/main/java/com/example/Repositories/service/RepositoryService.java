package com.example.Repositories.service;

import com.example.Repositories.client.RepositoryClient;
import com.example.Repositories.exception.RepositoryNotFoundException;
import com.example.Repositories.mapper.RepositoryMapper;
import com.example.Repositories.model.GitHubRepository;
import com.example.Repositories.model.GitHubRepositoryDto;
import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.repository.GitHubJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class RepositoryService {
    private final RepositoryClient repositoryClient;
    private final RepositoryMapper repositoryMapper;
    private final GitHubJpaRepository jpaRepository;

    @Cacheable(cacheNames = "gitHubRepositories", key = "#owner + '/' + #repositoryName", unless = "#result == null")
    public GitHubRepositoryDto getRepository(String owner, String repositoryName) {
        log.info("Fetching the owner repository named: owner={}", owner);
        CompletableFuture<GitHubRepository> repositoryFuture = CompletableFuture.supplyAsync(() -> jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new));
        GitHubRepository localRepo = repositoryFuture.join();
        return repositoryMapper.toDto(localRepo);
    }

    @CachePut(cacheNames = "gitHubRepositories", key = "#owner + '/' + #repositoryName", unless = "#result == null")
    public GitHubRepositoryDto createRepository(String owner, String repositoryName) {
        log.info("Saving the owner repository in local dataBase: owner={}", owner);
        CompletableFuture<GitHubResponseDto> responseFuture = CompletableFuture.supplyAsync(() -> repositoryClient.getRepo(owner, repositoryName));
        GitHubResponseDto responseDto = responseFuture.join();
        GitHubRepository entityRepository = repositoryMapper.toEntity(responseDto);
        GitHubRepository savedRepository = jpaRepository.save(entityRepository);
        return repositoryMapper.toDto(savedRepository);
    }

    @CacheEvict(cacheNames = "gitHubRepositories", key = "#owner + '/' + #repositoryName")
    public GitHubRepositoryDto updateRepository(String owner, String repositoryName) {
        log.info("Updating repository in local dataBase: owner={}, repositoryName={}", owner, repositoryName);
        CompletableFuture<GitHubRepository> repositoryFuture = CompletableFuture.supplyAsync(() -> jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new));
        CompletableFuture<GitHubResponseDto> responseFuture = CompletableFuture.supplyAsync(() -> repositoryClient.getRepo(owner, repositoryName));

        GitHubRepository repository = repositoryFuture.join();
        GitHubResponseDto responseDto = responseFuture.join();
        repository.update(responseDto);
        GitHubRepository savedRepository = jpaRepository.save(repository);
        return repositoryMapper.toDto(savedRepository);
    }

    @CacheEvict(cacheNames = "gitHubRepositories", key = "#owner + '/' + #repositoryName")
    public void deleteRepository(String owner, String repositoryName) {
        log.info("Deleting repository from local dataBase: owner={}, repositoryName={}", owner, repositoryName);
        CompletableFuture<GitHubRepository> repositoryFuture = CompletableFuture.supplyAsync(() -> jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new));

        GitHubRepository repository = repositoryFuture.join();
        jpaRepository.delete(repository);
    }
}
