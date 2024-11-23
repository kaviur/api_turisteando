package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class FavoriteResponseDto implements IDto {

    private Long id;
    private LocalDateTime createdAt;
    private boolean status;
    private Long userId;
    private Long touristPlanId;
}
