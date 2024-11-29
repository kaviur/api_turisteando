package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.RoleRequestDto;
import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface IUserService extends CrudService<UserRequestDto, UserResponseDto, Long> {

    Iterable<UserResponseDto> getAllByFilters(UserRequestDto dto);

    UserResponseDto getCurrentUser(Authentication authentication);

    @Transactional
    UserResponseDto toggleUserRole(Long id);

    UserResponseDto updateRole(Long id, RoleRequestDto roleRequestDto);
}
