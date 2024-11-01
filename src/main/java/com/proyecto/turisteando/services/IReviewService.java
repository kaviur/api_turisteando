package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;

public interface IReviewService extends CrudService<ReviewRequestDto, ReviewResponseDto, Long>{
    Iterable<ReviewResponseDto> getAllByPlan(Long idPlan);

    Iterable<ReviewResponseDto> getAllByRating(Long idPlan, int rating);
}
