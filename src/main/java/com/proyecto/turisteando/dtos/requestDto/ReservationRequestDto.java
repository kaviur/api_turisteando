package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;


@Value
@AllArgsConstructor
public class ReservationRequestDto implements IDto {

    @NotNull(message = "El ID del plan turístico no puede estar vacío")
    private Long touristPlanId;

    @NotNull(message = "El estado de la reserva no puede estar vacío")
    private boolean status;

    @NotNull(message = "La fecha de inicio de la reserva no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio de la reserva debe ser una fecha presente o futura  ")
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de fin de la reserva no puede estar vacía")
    @FutureOrPresent(message = "La fecha de fin de la reserva debe ser una fecha presente o futura")
    private LocalDateTime endDate;

    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    @Positive
    private int peopleCount;
}
