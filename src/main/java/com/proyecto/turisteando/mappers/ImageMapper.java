package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.ImageRequestDto;
import com.proyecto.turisteando.entities.ImageEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {
    ImageEntity toEntity(ImageRequestDto imageRequestDto);

    ImageRequestDto toDto(ImageEntity imageEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ImageEntity partialUpdate(ImageRequestDto imageRequestDto, @MappingTarget ImageEntity imageEntity);
}
