package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;

public interface IReservationService extends CrudService<ReservationRequestDto, ReservationResponseDto, Long> {

}
