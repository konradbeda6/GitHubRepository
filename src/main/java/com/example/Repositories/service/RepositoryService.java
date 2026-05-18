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
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RepositoryService {
    private final RepositoryClient repositoryClient;
    private final RepositoryMapper repositoryMapper;
    private final GitHubJpaRepository jpaRepository;

    public GitHubRepositoryDto getRepository(String owner, String repositoryName) {
        log.info("Fetching the owner repository named: owner={}", owner);
        GitHubRepository localRepo = jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(RepositoryNotFoundException::new);
        return repositoryMapper.toDto(localRepo);
    }

    public GitHubRepositoryDto createRepository(String owner, String repositoryName) {
        log.info("Saving the owner repository in local dataBase: owner={}", owner);
        GitHubResponseDto repo = repositoryClient.getRepo(owner, repositoryName);
        GitHubRepository entityRepository = repositoryMapper.toEntity(repo);
        GitHubRepository savedRepository = jpaRepository.save(entityRepository);
        return repositoryMapper.toDto(savedRepository);
    }
}
