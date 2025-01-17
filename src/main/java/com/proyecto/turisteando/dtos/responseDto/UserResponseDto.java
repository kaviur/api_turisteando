package com.proyecto.turisteando.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private Boolean isActive;


}
