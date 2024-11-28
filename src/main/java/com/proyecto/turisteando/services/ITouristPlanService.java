package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;

import java.util.List;

public interface ITouristPlanService extends CrudService<TouristPlanRequestDto, TouristPlanResponseDto, Long> {

    Iterable<TouristPlanResponseDto> getAllByFilters(TouristPlanRequestDto dto);
    List<TouristPlanResponseDto> findAllFavoritesByUserId (Long userId);
    void addUsersFavorites (Long userId, Long touristPlanId);
    void deleteUsersFavorites (Long userId, Long touristPlanId);

}
