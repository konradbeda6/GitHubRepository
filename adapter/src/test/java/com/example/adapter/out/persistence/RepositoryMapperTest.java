package com.example.adapter.out.persistence;

import com.example.model.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryMapperTest {

    private RepositoryMapper repositoryMapper;

    @BeforeEach
    void setUp() {
        repositoryMapper = Mappers.getMapper(RepositoryMapper.class);
    }

    @Test
    void toPojo_FromGitHubResponseDto_ReturnRepository() {
        GitHubResponseDto response = new GitHubResponseDto(new OwnerDto("octocat"), "Hello-World", "octocat/Hello-World", "My first repository", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));

        Repository result = repositoryMapper.toPojo(response);

        assertEquals("octocat", result.getOwner());
        assertEquals("Hello-World", result.getRepositoryName());
        assertEquals("octocat/Hello-World", result.getFullName());
        assertEquals("My first repository", result.getDescription());
        assertEquals("https://github.com/octocat/Hello-World.git", result.getCloneUrl());
        assertEquals(3572L, result.getWatchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.getCreatedAt());
    }

    @Test
    void toPojo_FromEntity_ReturnRepository() {
        GitHubRepositoryEntity entity = new GitHubRepositoryEntity(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));

        Repository result = repositoryMapper.toPojo(entity);

        assertEquals("octocat", result.getOwner());
        assertEquals("Hello-World", result.getRepositoryName());
        assertEquals("octocat/Hello-World", result.getFullName());
        assertEquals("My first repository", result.getDescription());
        assertEquals("https://github.com/octocat/Hello-World.git", result.getCloneUrl());
        assertEquals(3572L, result.getWatchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.getCreatedAt());
    }

    @Test
    void toEntity_FromRepository_ReturnEntity() {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));

        GitHubRepositoryEntity result = repositoryMapper.toEntity(repository);

        assertNull(result.getId());
        assertEquals("octocat", result.getOwner());
        assertEquals("Hello-World", result.getRepositoryName());
        assertEquals("octocat/Hello-World", result.getFullName());
        assertEquals("My first repository", result.getDescription());
        assertEquals("https://github.com/octocat/Hello-World.git", result.getCloneUrl());
        assertEquals(3572L, result.getWatchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.getCreatedAt());
    }
}