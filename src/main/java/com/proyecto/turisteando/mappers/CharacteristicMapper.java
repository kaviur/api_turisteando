package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.CharacteristicRequestDto;
import com.proyecto.turisteando.dtos.responseDto.CharacteristicResponseDto;
import com.proyecto.turisteando.entities.CharacteristicEntity;

import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CharacteristicMapper {
    CharacteristicEntity toEntity(CharacteristicRequestDto characteristicDto);

    CharacteristicResponseDto toDto(CharacteristicEntity characteristicEntity);
    List<CharacteristicResponseDto> toDtoList(List<CharacteristicEntity> characteristicEntityList);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CharacteristicEntity partialUpdate(CharacteristicRequestDto characteristicDto, @MappingTarget CharacteristicEntity characteristicEntity);

}
