package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;

import java.util.List;

public interface IUserService extends CrudService<UserRequestDto, UserResponseDto, Long> {

    Iterable<UserResponseDto> getAllByFilters(UserRequestDto dto);
}
