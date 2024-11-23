package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.FavoriteRequestDto;
import com.proyecto.turisteando.dtos.responseDto.FavoriteResponseDto;

public interface IFavoriteService extends CrudService<FavoriteRequestDto, FavoriteResponseDto, Long> {
}
