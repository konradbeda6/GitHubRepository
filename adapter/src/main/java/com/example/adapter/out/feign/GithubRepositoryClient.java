package com.example.adapter.out.feign;

import com.example.adapter.out.persistence.GitHubResponseDto;
import com.example.adapter.out.persistence.RepositoryMapper;
import com.example.api.port.out.GitHubRepositoryProvider;
import com.example.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubRepositoryClient implements GitHubRepositoryProvider {

    private final RepositoryClient githubRepositoryApi;
    private final RepositoryMapper mapper;

    @Cacheable(value = "githubRepositories", key = "#owner + ':' + #repositoryName")
    @Override
    public Repository getRepository(String owner, String repositoryName) {
        GitHubResponseDto dto = githubRepositoryApi.getRepo(owner, repositoryName);
        return mapper.toPojo(dto);
    }

}
