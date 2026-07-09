package com.example.adapter.out.feign;

import com.example.adapter.out.persistence.GitHubResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gitHubClient", configuration = ClientConfiguration.class, fallbackFactory = RepositoryClientFallbackFactory.class)
public interface RepositoryClient {

    @GetMapping("/repos/{owner}/{repoName}")
    GitHubResponseDto getRepo(@PathVariable String owner, @PathVariable String repoName);
}
