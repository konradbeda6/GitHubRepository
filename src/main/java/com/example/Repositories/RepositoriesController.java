package com.example.Repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RepositoriesController {
    private final RepositoryClient repositoryClient;

    @GetMapping("/repositories/{owner}/{repositoryName}")
    public RepositoryDto getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        GitHubResponseDto repo = repositoryClient.getRepo(owner, repositoryName);
        return RepositoryDto.of(repo);
    }
}
