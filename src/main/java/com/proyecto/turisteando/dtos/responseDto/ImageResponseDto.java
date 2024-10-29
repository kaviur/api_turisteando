package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ImageResponseDto implements IDto, Serializable {
    private Long id;
    private String imageUrl;


}
