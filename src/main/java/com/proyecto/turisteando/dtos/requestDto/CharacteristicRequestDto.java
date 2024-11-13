package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor

public class CharacteristicRequestDto implements Serializable, IDto {

    @NotEmpty(message = "El nombre de la característica no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de la característica debe tener entre 3 y 50 caracteres")
    String name;

    @NotEmpty(message = "El icono de la característica no puede estar vacío")
    String icon;
}
