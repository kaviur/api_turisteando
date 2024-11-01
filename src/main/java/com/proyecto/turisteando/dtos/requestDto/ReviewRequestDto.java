package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
public class ReviewRequestDto implements Serializable, IDto {
    @NotNull(message = "El id de usuario no puede ser nulo")
    Long idUser;

    @NotNull(message = "El id del plan no puede ser nulo")
    Long planId;

    @NotNull(message = "El rating no puede ser nulo")
    @Min(value = 1, message = "El rating debe ser al menos 1")
    @Max(value = 5, message = "El rating no puede ser mayor a 5")
    int rating;

    @Size(max = 255, message = "El comentario no puede tener más de 255 caracteres")
    String comment;

}
