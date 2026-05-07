package com.example.Repositories.controller;

import com.example.Repositories.service.RepositoryService;
import com.example.Repositories.model.RepositoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Repositories", description = "Operations related to repositories ")
@Slf4j
@RequiredArgsConstructor
@RestController
public class RepositoriesController {
    private final RepositoryService repositoryService;

    @Operation(summary = "Get repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the repository")
    })
    @GetMapping("/repositories/{owner}/{repositoryName}")
    public RepositoryDto getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Fetching repository request: repositoryOwner={}, repositoryName={}", owner, repositoryName);
        return repositoryService.getRepository(owner, repositoryName);
    }
}
