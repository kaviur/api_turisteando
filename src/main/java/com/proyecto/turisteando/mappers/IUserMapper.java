package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.entities.UserEntity;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IUserMapper {

    UserEntity toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(UserEntity userEntity);

    Iterable<UserResponseDto> toDtoList(List<UserEntity> userEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", qualifiedByName = "encoderPassword")
    UserEntity partialUpdate(UserRequestDto userRequestDto, @MappingTarget UserEntity userEntity, @Context PasswordEncoder passwordEncoder);

    @Named("encoderPassword")
    default String encoderPassword(String password, @Context PasswordEncoder passwordEncoder) {
        return password != null ? passwordEncoder.encode(password) : null;
    }
}
