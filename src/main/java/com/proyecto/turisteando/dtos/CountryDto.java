package com.proyecto.turisteando.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CountryDto implements IDto, Serializable {

    private Long id;
    private String name;

}
