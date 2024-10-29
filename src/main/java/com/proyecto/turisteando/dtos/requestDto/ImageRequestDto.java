package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;


@Value
@AllArgsConstructor
public class ImageRequestDto implements Serializable, IDto {
    @NotNull
    @NotEmpty(message = "El url de la imagen no puede estar vacío")
    @Size(max = 255, message = "El url de la imagen no debería tener más 255 caracteres")
    String imageUrl;

    @NotNull(message = "El plan turístico  no puede estar vacío")
    private Long touristPlanId;


}
