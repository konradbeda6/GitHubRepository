package com.example.adapter.out.persistence;

import org.mapstruct.Mapper;
import com.example.model.Repository;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {
    @Mapping(target = "owner", source = "owner.login")
    Repository toPojo(GitHubResponseDto response);

    Repository toPojo(GitHubRepositoryEntity entity);

    GitHubRepositoryEntity toEntity(Repository repository);
}
