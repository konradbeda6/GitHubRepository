package com.example.Repositories.mapper;

import com.example.Repositories.model.GitHubResponseDto;
import com.example.Repositories.model.RepositoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    RepositoryDto toRepositoryDto(GitHubResponseDto gitHubResponseDto);
}
