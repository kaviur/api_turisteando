package com.proyecto.turisteando.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class ReservationResponseDto implements Serializable {

    Long id;
    LocalDateTime reservationDate;
    String status;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
