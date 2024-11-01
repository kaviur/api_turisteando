package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class ReservationResponseDto implements IDto {

    private Long id;
    private LocalDateTime createdAt;
    private boolean status;
    private LocalDate startDate;
    private LocalDate endDate;
    private int peopleCount;
    private Long touristPlanId;
}
