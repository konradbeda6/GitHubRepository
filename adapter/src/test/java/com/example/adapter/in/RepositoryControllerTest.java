package com.example.adapter.in;

import com.example.api.port.in.RepositoryResponseDto;
import com.example.api.port.in.RepositoryUseCase;
import com.example.model.exception.RepositoryNotFoundException;
import com.example.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepositoryController.class)
class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepositoryUseCase repositoryUseCase;

    @MockitoBean
    private RepositoryApiMapper repositoryApiMapper;

    @Test
    void getRepository_DataCorrect_ReturnRepository() throws Exception {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));

        RepositoryResponseDto dto = new RepositoryResponseDto("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));

        when(repositoryUseCase.getRepository("octocat", "Hello-World")).thenReturn(repository);
        when(repositoryApiMapper.toDto(repository)).thenReturn(dto);

        mockMvc.perform(get("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("octocat"))
                .andExpect(jsonPath("$.name").value("Hello-World"))
                .andExpect(jsonPath("$.full_name").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository on GitHub!"))
                .andExpect(jsonPath("$.clone_url").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.created_at").value("2011-01-26T19:01:12"));
    }

    @Test
    void createRepository_DataCorrect_ReturnRepository() throws Exception {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        RepositoryResponseDto dto = new RepositoryResponseDto("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        when(repositoryUseCase.createRepository("octocat", "Hello-World")).thenReturn(repository);
        when(repositoryApiMapper.toDto(repository)).thenReturn(dto);

        mockMvc.perform(post("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.owner").value("octocat"))
                .andExpect(jsonPath("$.name").value("Hello-World"))
                .andExpect(jsonPath("$.full_name").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository on GitHub!"))
                .andExpect(jsonPath("$.clone_url").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.created_at").value("2011-01-26T19:01:12"));
    }

    @Test
    void getRepository_RepositoryNotFound_Return404() throws Exception {
        when(repositoryUseCase.getRepository("octocat", "Hello-World")).thenThrow(new RepositoryNotFoundException());

        mockMvc.perform(get("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRepository_RepositoryNotFound_Return404() throws Exception {
        when(repositoryUseCase.createRepository("octocat", "Hello-World")).thenThrow(new RepositoryNotFoundException());

        mockMvc.perform(post("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRepository_DataCorrect_ReturnRepository() throws Exception {
        Repository repository = new Repository("octocat", "Hello-World", "octocat/Hello-World", "My first repository on GitHub!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));
        RepositoryResponseDto dto = new RepositoryResponseDto("octocat", "Hello-World", "octocat/Hello-World", "My first repository!", "https://github.com/octocat/Hello-World.git", 3572L, LocalDateTime.of(2011, 1, 26, 19, 1, 12));

        when(repositoryUseCase.updateRepository("octocat", "Hello-World")).thenReturn(repository);
        when(repositoryApiMapper.toDto(repository)).thenReturn(dto);

        mockMvc.perform(put("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("octocat"))
                .andExpect(jsonPath("$.name").value("Hello-World"))
                .andExpect(jsonPath("$.full_name").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("My first repository!"))
                .andExpect(jsonPath("$.clone_url").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.watchers").value(3572))
                .andExpect(jsonPath("$.created_at").value("2011-01-26T19:01:12"));
    }

    @Test
    void deleteRepository_RepositoryExists_Return204() throws Exception {
        mockMvc.perform(delete("/repositories/{owner}/{repositoryName}", "octocat", "Hello-World"))
                .andExpect(status().isNoContent());
    }
}