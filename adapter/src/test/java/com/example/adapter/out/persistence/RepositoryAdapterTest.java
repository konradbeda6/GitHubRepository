package com.example.adapter.out.persistence;

import com.example.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepositoryAdapterTest {

    private GitHubJpaRepository jpaRepository;
    private RepositoryMapper repositoryMapper;
    private RepositoryAdapter repositoryAdapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(GitHubJpaRepository.class);
        repositoryMapper = Mappers.getMapper(RepositoryMapper.class);
        repositoryAdapter = new RepositoryAdapter(jpaRepository, repositoryMapper);
    }

    @Test
    void findByOwnerAndRepositoryName_RepositoryExists_ReturnRepository() {
        GitHubRepositoryEntity entity = new GitHubRepositoryEntity(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(jpaRepository.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(entity));

        Optional<Repository> result = repositoryAdapter.findByOwnerAndRepositoryName("octocat", "Hello-World");

        assertTrue(result.isPresent());
        assertEquals("octocat", result.get().getOwner());
        assertEquals("Hello-World", result.get().getRepositoryName());
    }

    @Test
    void findByOwnerAndRepositoryName_RepositoryNotExists_ReturnEmptyOptional() {
        when(jpaRepository.findByOwnerAndRepositoryName("octocat", "Hello-World"))
                .thenReturn(Optional.empty());

        Optional<Repository> result = repositoryAdapter.findByOwnerAndRepositoryName("octocat", "Hello-World");

        assertTrue(result.isEmpty());
    }

    @Test
    void save_DataCorrect_ReturnRepository() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        GitHubRepositoryEntity savedEntity = new GitHubRepositoryEntity(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(jpaRepository.save(any(GitHubRepositoryEntity.class))).thenReturn(savedEntity);

        Repository result = repositoryAdapter.save(repository);

        assertEquals("octocat", result.getOwner());
        assertEquals("Hello-World", result.getRepositoryName());
        assertEquals("https://github.com/octocat/Hello-World.git", result.getCloneUrl());

        verify(jpaRepository).save(any(GitHubRepositoryEntity.class));
    }

    @Test
    void deleteByOwnerAndRepositoryName_DataCorrect_DeleteCalled() {
        repositoryAdapter.deleteByOwnerAndRepositoryName("octocat", "Hello-World");

        verify(jpaRepository).deleteByOwnerAndRepositoryName("octocat", "Hello-World");
    }
}