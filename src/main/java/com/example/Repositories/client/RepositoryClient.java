package com.example.Repositories.client;

import com.example.Repositories.model.GitHubResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "gitHubName", url = "https://api.github.com", configuration = CustomErrorDecoder.class)
public interface RepositoryClient {

    @GetMapping("/repos/{owner}/{repoName}")
    GitHubResponseDto getRepo(@PathVariable String owner, @PathVariable String repoName);
}
