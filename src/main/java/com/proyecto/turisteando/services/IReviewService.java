package com.proyecto.turisteando.services;

import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewService extends CrudService<ReviewRequestDto, ReviewResponseDto, Long>{
    Iterable<ReviewResponseDto> getAllByPlan(Long idPlan);
    Iterable<ReviewResponseDto> getAllByRating(Long idPlan, int rating);
    Page<ReviewResponseDto> getAllByPlanP(Long idPlan, Pageable pageable);

    Page<ReviewResponseDto> getAllByUser(Long idUser, Pageable pageable);

}
