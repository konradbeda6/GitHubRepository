package com.example.adapter.out.feign;

import com.example.adapter.out.persistence.GitHubResponseDto;
import com.example.model.exception.InternalServerException;
import com.example.model.exception.RepositoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RepositoryClientFallbackFactory implements FallbackFactory<RepositoryClient> {

    @Override
    public RepositoryClient create(Throwable cause) {
        log.error("An exception occurred when calling the RepositoryClient", cause);
        return new RepositoryClient() {
            @Override
            public GitHubResponseDto getRepo(String owner, String repoName) {
                log.info("[Fallback] getRepo");
                if (cause instanceof RepositoryNotFoundException) {
                    throw (RepositoryNotFoundException) cause;
                }
                throw new InternalServerException();
            }
        };
    }
}
