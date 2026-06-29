package com.example.Repositories;


import com.example.Repositories.client.RepositoryClient;
import com.example.Repositories.exception.RepositoryNotFoundException;
import com.example.Repositories.mapper.RepositoryMapper;
import com.example.Repositories.model.GitHubRepository;
import com.example.Repositories.model.GitHubRepositoryDto;
import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.model.OwnerDto;
import com.example.Repositories.repository.GitHubJpaRepository;
import com.example.Repositories.service.RepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import java.util.concurrent.CompletionException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryServiceTest {

    private GitHubJpaRepository jpaRepository;
    private RepositoryMapper repositoryMapper;
    private RepositoryClient repositoryClient;
    private RepositoryService repositoryService;

    @BeforeEach
    void setUp() {
        jpaRepository = Mockito.mock(GitHubJpaRepository.class);
        repositoryMapper = Mappers.getMapper(RepositoryMapper.class);
        repositoryClient = Mockito.mock(RepositoryClient.class);
        repositoryService = new RepositoryService(repositoryClient, repositoryMapper, jpaRepository);
    }

    @Test
    void getRepository_DataCorrect_ReturnRepository() {
        //given
        GitHubRepository repository = new GitHubRepository(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(jpaRepository.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(repository));
        //when
        GitHubRepositoryDto result = repositoryService.getRepository("octocat", "Hello-World");
        //then
        assertEquals(1L, result.id());
        assertEquals("octocat/Hello-World", result.fullName());
        assertEquals("My first repository on GitHub!", result.description());
        assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl());
        assertEquals(3572L, result.watchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.createdAt());
    }

    @Test
    void createRepository_DataCorrect_ReturnRepository() {
        //given
        GitHubResponseDto responseDto = new GitHubResponseDto(new OwnerDto("octocat"), "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        GitHubRepository repository = new GitHubRepository(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryClient.getRepo("octocat", "Hello-World")).thenReturn(responseDto);
        when(jpaRepository.save(any(GitHubRepository.class))).thenReturn(repository);
        //when
        GitHubRepositoryDto result = repositoryService.createRepository("octocat", "Hello-World");
        //then
        assertEquals(1L, result.id());
        assertEquals("octocat/Hello-World", result.fullName());
        assertEquals("My first repository on GitHub!", result.description());
        assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl());
        assertEquals(3572L, result.watchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.createdAt());
    }

    @Test
    void getRepository_RepositoryNotFound_RepositoryNotFoundExceptionThrown() {
        //given
        when(jpaRepository.findByOwnerAndRepositoryName("otocat", "Hello-World")).thenReturn(Optional.empty());
        //when
        CompletionException exception = assertThrows(CompletionException.class, () -> repositoryService.getRepository("otocat", "Hello-World"));
        RepositoryNotFoundException cause = assertInstanceOf(RepositoryNotFoundException.class, exception.getCause());
        //then
        assertEquals("Repository not found", cause.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, cause.getStatus());
    }


    @Test
    void createRepository_RepositoryNotFound_RepositoryNotFoundExceptionThrown() {
        //given
        when(repositoryClient.getRepo("otocat", "Hello-World")).thenThrow(new RepositoryNotFoundException());
        //when
        CompletionException exception = assertThrows(CompletionException.class, () -> repositoryService.createRepository("otocat", "Hello-World"));
        RepositoryNotFoundException cause = assertInstanceOf(RepositoryNotFoundException.class, exception.getCause());
        //then
        assertEquals("Repository not found", cause.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, cause.getStatus());
    }

    @Test
    void updateRepository_DataCorrect_RepositoryUpdated() {
        //given
        GitHubRepository repository = new GitHubRepository(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        GitHubResponseDto responseDto = new GitHubResponseDto(new OwnerDto("octocat"), "Hello-World", "octocat/Hello-World", "My first repository!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(jpaRepository.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(repository));
        when(jpaRepository.save(any(GitHubRepository.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repositoryClient.getRepo("octocat", "Hello-World")).thenReturn(responseDto);
        //when
        GitHubRepositoryDto repositoryDto = repositoryService.updateRepository("octocat", "Hello-World");
        //then
        assertEquals(1L, repositoryDto.id());
        assertEquals("octocat/Hello-World", repositoryDto.fullName());
        assertEquals("My first repository!", repositoryDto.description());
        assertEquals("https://github.com/octocat/Hello-World.git", repositoryDto.cloneUrl());
        assertEquals(3572L, repositoryDto.watchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), repositoryDto.createdAt());
    }

    @Test
    void deleteRepository_RepositoryExists_RepositoryDeleted() {
        //given
        GitHubRepository repository = new GitHubRepository(1L, "octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(jpaRepository.findByOwnerAndRepositoryName("octocat", "Hello-World")).thenReturn(Optional.of(repository));
        //when
        repositoryService.deleteRepository("octocat", "Hello-World");
        //then
        verify(jpaRepository).findByOwnerAndRepositoryName("octocat", "Hello-World");
        verify(jpaRepository).delete(repository);
    }

}
