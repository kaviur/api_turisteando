package com.proyecto.turisteando.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFavoriteTouristPlanRequestDto {

    private Long userId;

    private Long planId;

}