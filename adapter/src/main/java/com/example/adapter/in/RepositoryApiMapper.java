package com.example.adapter.in;

import com.example.api.port.in.RepositoryResponseDto;
import com.example.model.Repository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepositoryApiMapper {
    RepositoryResponseDto toDto(Repository repository);
}
