package com.example.Repositories.controller;

import com.example.Repositories.exception.ErrorMessage;
import com.example.Repositories.model.GitHubRepositoryDto;
import com.example.Repositories.service.RepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Repositories", description = "Operations related to repositories ")
@Slf4j
@RequiredArgsConstructor
@RestController
public class RepositoryController {
    private final RepositoryService repositoryService;


    @Operation(summary = "Get repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the repository"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/repositories/{owner}/{repositoryName}")
    public GitHubRepositoryDto getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Fetching repository request: repositoryOwner={}, repositoryName={}", owner, repositoryName);
        return repositoryService.getRepository(owner, repositoryName);
    }

    @Operation(summary = "Create repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the repository"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/repositories/{owner}/{repositoryName}")
    public ResponseEntity<GitHubRepositoryDto> createRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Saving repository to local dataBase request: repositoryOwner={}, repositoryName={}", owner, repositoryName);

        GitHubRepositoryDto gitHubRepositoryDto = repositoryService.createRepository(owner, repositoryName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gitHubRepositoryDto);
    }

    @Operation(summary = "Update repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repository updated"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PutMapping("/repositories/{owner}/{repositoryName}")
    public ResponseEntity<GitHubRepositoryDto> updateRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Updating repository in local dataBase request: repositoryOwner={}, repositoryName={}", owner, repositoryName);


        GitHubRepositoryDto gitHubRepositoryDto = repositoryService.updateRepository(owner, repositoryName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(gitHubRepositoryDto);
    }

    @Operation(summary = "Delete repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Repository deleted"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping("/repositories/{owner}/{repositoryName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Deleting repository from local dataBase request: repositoryOwner={}, repositoryName={}", owner, repositoryName);

        repositoryService.deleteRepository(owner, repositoryName);
    }
}
