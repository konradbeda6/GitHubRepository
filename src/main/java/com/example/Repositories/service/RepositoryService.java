package com.example.Repositories.service;

import com.example.Repositories.client.RepositoryClient;
import com.example.Repositories.exception.RepositoryNotFoundException;
import com.example.Repositories.mapper.RepositoryMapper;
import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.model.RepositoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RepositoryService {
    private final RepositoryClient repositoryClient;
    private final RepositoryMapper repositoryMapper;

    public RepositoryDto getRepository(String owner, String repositoryName) {
        log.info("Fetching the owner repository named: owner={}", owner);
        GitHubResponseDto repo = repositoryClient.getRepo(owner, repositoryName);

        return repositoryMapper.toRepositoryDto(repo);
    }
}
