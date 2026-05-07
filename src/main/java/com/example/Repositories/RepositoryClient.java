package com.example.Repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gitHubName", url = "https://api.github.com")
public interface RepositoryClient {

    @GetMapping("/repos/{owner}/{repoName}")
    GitHubResponseDto getRepo(@PathVariable String owner, @PathVariable String repoName);
}
