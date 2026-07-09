package com.example.adapter.out.persistence;

import com.example.api.port.out.RepositoryProvider;
import com.example.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepositoryAdapter implements RepositoryProvider {
    private final GitHubJpaRepository jpaRepository;
    private final RepositoryMapper repositoryMapper;

    @Cacheable(value = "localRepositories", key = "#owner + ':' + #repositoryName")
    @Override
    public Optional<Repository> findByOwnerAndRepositoryName(String owner, String repositoryName) {
        return jpaRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .map(repositoryMapper::toPojo);
    }

    @CachePut(value = "localRepositories", key = "#repository.owner + ':' + #repository.repositoryName")
    @Override
    public Repository save(Repository repository) {
        GitHubRepositoryEntity entity = repositoryMapper.toEntity(repository);
        GitHubRepositoryEntity saved = jpaRepository.save(entity);
        return repositoryMapper.toPojo(saved);
    }

    @CacheEvict(value = "localRepositories", key = "#owner + ':' + #repositoryName")
    @Override
    public void deleteByOwnerAndRepositoryName(String owner, String repositoryName) {
        jpaRepository.deleteByOwnerAndRepositoryName(owner, repositoryName);
    }
}
