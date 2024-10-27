package com.proyecto.turisteando.dtos.requestDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Value;


import java.io.Serializable;
import java.time.LocalDateTime;


@Value
@AllArgsConstructor
public class ReservationDto implements Serializable {
    //@NotNull
    //@NotEmpty(message = "El ID de usuario no puede estar vacío")
    //Integer idUser;

    //@NotNull(message = "El ID del plan turístico no puede estar vacío")
    //Integer idPlan;

    @NotNull(message = "La fecha de la reserva no puede estar vacía")
    LocalDateTime reservationDate;

    @NotNull(message = "El estado de la reserva no puede estar vacío")
    @Pattern(regexp = "Confirmed|Pending|Cancelled", message = "El estado debe ser 'Confirmed', 'Pending' o 'Cancelled'")
    String status;

    @NotNull(message = "La fecha de inicio de la reserva no puede estar vacía")
    LocalDateTime startDate;

    @NotNull(message = "La fecha de fin de la reserva no puede estar vacía")
    LocalDateTime endDate;
}
