package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import org.springframework.security.core.Authentication;

public interface IUserService extends CrudService<UserRequestDto, UserResponseDto, Long> {

    Iterable<UserResponseDto> getAllByFilters(UserRequestDto dto);
    UserResponseDto getCurrentUser(Authentication authentication);
}
