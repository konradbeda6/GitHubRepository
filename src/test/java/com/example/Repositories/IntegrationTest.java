package com.example.Repositories;

import com.example.Repositories.model.GitHubRepository;
import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.model.OwnerDto;
import com.example.Repositories.repository.GitHubJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GitHubJpaRepository jpaRepository;

    @AfterEach
    void clear() {
        WireMock.reset();
    }

    @Test
    void createRepository_DataCorrect_ReturnRepository() throws Exception {
        //given
        GitHubResponseDto responseDto = new GitHubResponseDto(new OwnerDto("octocat"), "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        String response = objectMapper.writeValueAsString(responseDto);
        stubFor(WireMock.get(urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(okJson(response)));
        //when&then
        mockMvc.perform(post("/repositories/octocat/Hello-World"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository on GitHub!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12"));
    }

    @Test
    void createRepository_RepositoryNotFound_RepositoryNotFoundExceptionThrown() throws Exception {
        //given
        stubFor(WireMock.get(urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(notFound()));
        //when&then
        mockMvc.perform(post("/repositories/octocat/Hello-World"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Repository not found"));
    }

    @Test
    void getRepository_DataCorrect_ReturnRepository() throws Exception {
        //given
        GitHubRepository repository = new GitHubRepository(null, "octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        GitHubRepository saved = jpaRepository.save(repository);
        //when&then
        mockMvc.perform(get("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository on GitHub!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12"));
    }
}