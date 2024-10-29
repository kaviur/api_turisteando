package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;


@Value
@AllArgsConstructor
public class CategoryRequestDto implements Serializable, IDto {
    @NotNull
    @NotEmpty(message = "El nombre de la categoría no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de la categoría debe tener entre 3 y 50 caracteres")
    String name;

    @Size(max = 255, message = "La descripción de la categoría no puede tener más de 255 caracteres")
    String description;

    @NotEmpty(message = "La imagen de la categoría no puede estar vacía")
    String image;

}
