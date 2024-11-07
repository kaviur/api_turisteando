package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.entities.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IUserMapper {

    UserEntity toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(UserEntity userEntity);

    List<UserRequestDto> toDtoList(List<UserEntity> userEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(UserRequestDto userRequestDto, @MappingTarget UserEntity userEntity);
}
