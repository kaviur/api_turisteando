package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.CountryDto;
import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;
import com.proyecto.turisteando.entities.CountryEntity;
import com.proyecto.turisteando.entities.ReviewEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    ReviewResponseDto toDto(ReviewEntity reviewEntity);

    ReviewEntity toEntity(ReviewRequestDto reviewRequestDto);

    ReviewRequestDto toRequestDto(ReviewEntity review);

    @Mapping(source = "id", target = "idReview")
    ReviewResponseDto toResponseDto(ReviewEntity review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReviewEntity partialUpdate(ReviewRequestDto reviewRequestDto, @MappingTarget ReviewEntity review);
}
