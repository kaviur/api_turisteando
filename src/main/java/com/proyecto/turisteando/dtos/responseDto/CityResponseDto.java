package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;

import java.io.Serializable;

public class CityResponseDto implements IDto, Serializable {

    private Long id;
    private String name;
    private Long countryId;

}