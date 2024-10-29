package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;

public interface ITouristPlanService extends CrudService<TouristPlanRequestDto, TouristPlanResponseDto, Long> {

    Iterable<TouristPlanResponseDto> getAllByFilters(TouristPlanRequestDto dto);

}
