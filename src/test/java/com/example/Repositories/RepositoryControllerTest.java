package com.example.Repositories;

import com.example.Repositories.exception.RepositoryNotFoundException;
import com.example.Repositories.model.GitHubRepositoryDto;
import com.example.Repositories.service.RepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RepositoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    RepositoryService repositoryService;

    @Test
    void getRepository_DataCorrect_ReturnRepository() throws Exception {
        //given
        GitHubRepositoryDto repositoryDto = new GitHubRepositoryDto(1L, "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryService.getRepository("octocat", "Hello-World")).thenReturn(repositoryDto);
        //when&then
        mockMvc.perform(get("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository on GitHub!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12"));
    }

    @Test
    void createRepository_DataCorrect_ReturnRepository() throws Exception {
        //given
        GitHubRepositoryDto repositoryDto = new GitHubRepositoryDto(1L, "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryService.createRepository("octocat", "Hello-World")).thenReturn(repositoryDto);
        //when&then
        mockMvc.perform(post("/repositories/octocat/Hello-World"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository on GitHub!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12"));
    }

    @Test
    void getRepository_RepositoryNotFound_Return404() throws Exception {
        //given
        when(repositoryService.getRepository("octocat", "Hello-World"))
                .thenThrow(new RepositoryNotFoundException());
        //when&then
        mockMvc.perform(get("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRepository_RepositoryNotFound_Return404() throws Exception {
        //given
        when(repositoryService.createRepository("octocat", "Hello-World"))
                .thenThrow(new RepositoryNotFoundException());
        //when&then
        mockMvc.perform(post("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRepository_DataCorrect_ReturnRepository() throws Exception {
        //given
        GitHubRepositoryDto repositoryDto = new GitHubRepositoryDto(1L, "octocat/Hello-World", "My first repository!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryService.updateRepository("octocat", "Hello-World"))
                .thenReturn(repositoryDto);

        //when&then
        mockMvc.perform(put("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12"));
    }

    @Test
    void deleteRepository_RepositoryExists_Return204() throws Exception {
        //when&then
        mockMvc.perform(delete("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isNoContent());
    }
}
