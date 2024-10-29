package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.CategoryRequestDto;
import com.proyecto.turisteando.dtos.responseDto.CategoryResponseDto;
import com.proyecto.turisteando.entities.CategoryEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryEntity toEntity(CategoryRequestDto categoryDto);

    CategoryResponseDto toDto(CategoryEntity categoryEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryEntity partialUpdate(CategoryRequestDto categoryDto, @MappingTarget CategoryEntity categoryEntity);
}
