package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;

import java.time.LocalDate;
import java.util.List;


public interface IReservationService extends CrudService<ReservationRequestDto, ReservationResponseDto, Long> {

    Iterable<ReservationResponseDto> getReservationsByTouristPlan(Long touristPlanId);
    Iterable<ReservationResponseDto> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    Iterable<ReservationResponseDto> findByUserIdAndStatus(Long userId, boolean status);
}
