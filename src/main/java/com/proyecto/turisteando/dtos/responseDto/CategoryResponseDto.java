package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
public class CategoryResponseDto implements IDto, Serializable {
    Long id;
    String name;
    String description;
    ImageResponseDto image;
}
