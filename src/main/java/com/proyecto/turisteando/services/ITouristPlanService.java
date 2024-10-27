package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.IDto;

public interface ITouristPlanService extends ICrudService<IDto, Long> {

    Iterable<IDto> getAllByFilters(IDto iDto);

}
