package com.proyecto.turisteando.auth;

import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private UserResponseDto user;

}
