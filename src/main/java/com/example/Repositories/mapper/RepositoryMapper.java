package com.example.Repositories.mapper;

import com.example.Repositories.model.GitHubRepository;
import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.model.GitHubRepositoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {
    @Mapping(target = "owner", source = "owner.login")
    GitHubRepository toEntity(GitHubResponseDto gitHubResponseDto);
    GitHubRepositoryDto toDto(GitHubRepository gitHubRepository);
}
