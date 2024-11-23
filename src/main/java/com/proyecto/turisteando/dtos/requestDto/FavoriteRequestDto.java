package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FavoriteRequestDto implements IDto {
    @NotNull(message = "El ID del plan turístico no puede estar vacío")
    private Long touristPlanId;

    @NotNull(message = "El ID del usuario no puede estar vacío")
    private Long userId;

    @NotNull(message = "El estado no puede estar vacío")
    private boolean status;

}
