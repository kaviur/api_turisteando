package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class ReservationResponseDto implements IDto, Serializable {

    Long id;
    LocalDateTime reservationDate;
    String status;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
