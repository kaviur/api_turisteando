package com.proyecto.turisteando.dtos.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFavoriteTouristPlanRequestDto {

    private Long userId;

    private Long planId;

}