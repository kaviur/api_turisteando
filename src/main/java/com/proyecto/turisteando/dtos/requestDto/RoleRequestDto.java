package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.entities.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDto {
    @NotBlank(message = "El rol no puede estar vacío")
    private Role role;
}
