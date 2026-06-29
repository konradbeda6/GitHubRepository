package com.example.Repositories;

import com.example.Repositories.client.RepositoryClient;
import com.example.Repositories.exception.InternalServerException;
import com.example.Repositories.exception.RepositoryNotFoundException;
import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.model.OwnerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureWireMock(port = 0)
@SpringBootTest
@ActiveProfiles("test")
public class GitHubClientTest {

    @Autowired
    private RepositoryClient repositoryClient;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void resetWireMock() {
        WireMock.reset();
    }

    @Test
    void getRepo_DataCorrect_ReturnRepository() throws JsonProcessingException {
        //given
        GitHubResponseDto responseDto = new GitHubResponseDto(new OwnerDto("octocat"), "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        String response = objectMapper.writeValueAsString(responseDto);
        stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(okJson(response)));
        //when
        GitHubResponseDto result = repositoryClient.getRepo("octocat", "Hello-World");
        //then
        assertEquals("octocat", result.owner().login());
        assertEquals("Hello-World", result.repositoryName());
        assertEquals("octocat/Hello-World", result.fullName());
        assertEquals("My first repository on GitHub!", result.description());
        assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl());
        assertEquals(3572L, result.watchers());
        assertEquals(LocalDateTime.of(2011, 1, 26, 19, 1, 12), result.createdAt());
        verify(getRequestedFor(urlEqualTo("/repos/octocat/Hello-World")));
    }

    @Test
    void getRepo_RepositoryNotFound_RepositoryNotFoundExceptionThrown() {
        //given
        stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(aResponse().withStatus(404)));
        //when&then
        assertThrows(
                RepositoryNotFoundException.class,
                () -> repositoryClient.getRepo("octocat", "Hello-World")
        );
        verify(1, getRequestedFor(urlEqualTo("/repos/octocat/Hello-World")));
    }

    @Test
    void getRepo_ServiceUnavailable_RetryExecuted() {
        //given
        stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(aResponse().withStatus(503)));
        // when
        assertThrows(
                InternalServerException.class,
                () -> repositoryClient.getRepo("octocat", "Hello-World")
        );
        //then
        verify(3, getRequestedFor(urlEqualTo("/repos/octocat/Hello-World")));
    }
}
