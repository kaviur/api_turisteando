package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ITouristPlanService extends CrudService<TouristPlanRequestDto, TouristPlanResponseDto, Long> {

    Iterable<TouristPlanResponseDto> getAllByFilters(TouristPlanRequestDto dto);

    List<TouristPlanResponseDto> findAllFavoritesByUser(UserEntity user);

    void addUsersFavorites(Long userId, Long touristPlanId);

    void deleteUsersFavorites(Long userId, Long touristPlanId);

}
