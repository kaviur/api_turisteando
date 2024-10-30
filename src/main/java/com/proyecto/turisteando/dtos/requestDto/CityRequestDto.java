package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CityRequestDto implements IDto, Serializable {

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "El id del país no puede estar vacío")
    @Positive(message = "El id del país debe ser mayor a cero")
    private Long countryId;

}
