package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Value
@AllArgsConstructor
public class ReservationRequestDto implements IDto {

    @NotNull(message = "El ID del plan turístico no puede estar vacío")
    private Long touristPlanId;

    @NotNull(message = "El ID del usuario no puede estar vacío")
    private Long userId;

    @NotNull(message = "El estado de la reserva no puede estar vacío")
    private boolean status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "La fecha de inicio de la reserva no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio de la reserva debe ser una fecha presente o futura  ")
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "La fecha de fin de la reserva no puede estar vacía")
    @FutureOrPresent(message = "La fecha de fin de la reserva debe ser una fecha presente o futura")
    private LocalDate endDate;

    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    @Positive
    private int peopleCount;
}
