package com.example.adapter.config;

import com.example.adapter.in.TransactionalRepositoryUseCase;
import com.example.api.port.in.RepositoryUseCase;
import com.example.api.port.out.GitHubRepositoryProvider;
import com.example.api.port.out.RepositoryProvider;
import com.example.domain.RepositoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    RepositoryService repositoryService(
            GitHubRepositoryProvider gitHubRepositoryProvider,
            RepositoryProvider repositoryProvider
    ) {
        return new RepositoryService(gitHubRepositoryProvider, repositoryProvider);
    }

    @Bean
    RepositoryUseCase repositoryUseCase(RepositoryService repositoryService) {
        return new TransactionalRepositoryUseCase(repositoryService);
    }
}
