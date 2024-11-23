package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.FavoriteRequestDto;
import com.proyecto.turisteando.dtos.responseDto.FavoriteResponseDto;
import com.proyecto.turisteando.entities.FavoriteEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface FavoriteMapper {

    FavoriteEntity toEntity(FavoriteRequestDto favoriteRequestDto);

    @Mapping(target = "touristPlanId", source = "touristPlan.id")
    @Mapping(target = "userId", source = "user.id")
    FavoriteResponseDto toDto(FavoriteEntity favoriteEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FavoriteEntity partialUpdate(FavoriteRequestDto favoriteRequestDto, @MappingTarget FavoriteEntity favoriteEntity);
}
