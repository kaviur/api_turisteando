package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.CountryDto;
import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CityResponseDto implements IDto, Serializable {

    private Long id;
    private String name;
    private CountryDto country;

}