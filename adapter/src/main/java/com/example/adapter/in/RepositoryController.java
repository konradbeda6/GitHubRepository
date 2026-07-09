package com.example.adapter.in;

import com.example.adapter.in.exception.ErrorMessage;
import com.example.api.port.in.RepositoryResponseDto;
import com.example.api.port.in.RepositoryUseCase;
import com.example.model.Repository;
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
    private final RepositoryUseCase repositoryService;
    private final RepositoryApiMapper repositoryApiMapper;


    @Operation(summary = "Get repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the repository"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/repositories/{owner}/{repositoryName}")
    public RepositoryResponseDto getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Fetching repository request: repositoryOwner={}, repositoryName={}", owner, repositoryName);
        Repository repository = repositoryService.getRepository(owner, repositoryName);
        return repositoryApiMapper.toDto(repository);
    }

    @Operation(summary = "Create repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the repository"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/repositories/{owner}/{repositoryName}")
    public ResponseEntity<RepositoryResponseDto> createRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Saving repository to local dataBase request: repositoryOwner={}, repositoryName={}", owner, repositoryName);

        Repository repository = repositoryService.createRepository(owner, repositoryName);
        RepositoryResponseDto responseDto = repositoryApiMapper.toDto(repository);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Operation(summary = "Update repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repository updated"),
            @ApiResponse(responseCode = "404", description = "Repository not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PutMapping("/repositories/{owner}/{repositoryName}")
    public ResponseEntity<RepositoryResponseDto> updateRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Updating repository in local dataBase request: repositoryOwner={}, repositoryName={}", owner, repositoryName);

        Repository repository = repositoryService.updateRepository(owner, repositoryName);
        RepositoryResponseDto responseDto = repositoryApiMapper.toDto(repository);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
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
