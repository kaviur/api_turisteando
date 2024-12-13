package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto implements IDto {

    private Long id;
    private LocalDateTime createdAt;
    private boolean status;
    private LocalDate startDate;
    private LocalDate endDate;
    private int peopleCount;
    private double totalPrice;
    private Long touristPlanId;
    private Long userId;
    private String touristPlanTitle;
}
