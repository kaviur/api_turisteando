package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;
import com.proyecto.turisteando.entities.ReviewEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.exceptions.customExceptions.ReviewNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.ReviewMapper;
import com.proyecto.turisteando.repositories.ReviewRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final TouristPlanRepository touristPlanRepository;
    private final ReviewMapper reviewMapper;


    @Override
    public Iterable<ReviewResponseDto> getAll() {
        return null;
    }

    @Override
    public ReviewResponseDto read(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        return reviewMapper.toResponseDto(reviewEntity);
    }

    @Override
    public ReviewResponseDto create(ReviewRequestDto reviewRequestDto) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(reviewRequestDto.getPlanId())
                    .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + reviewRequestDto.getPlanId() + " not found"));
            ReviewEntity reviewEntity = reviewMapper.toEntity(reviewRequestDto);
            reviewEntity.setTouristPlan(touristPlan);
            reviewEntity.setUser(reviewRequestDto.getIdUser());
            return reviewMapper.toResponseDto(reviewRepository.save(reviewEntity));
        } catch (Exception e) {
            throw new ServiceException("Error creating review entity " + e.getMessage());
        }
    }

    @Override
    public ReviewResponseDto update(ReviewRequestDto reviewRequestDto, Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        ReviewEntity reviewEntityUpdated = reviewMapper.partialUpdate(reviewRequestDto, reviewEntity);
        return reviewMapper.toResponseDto(reviewRepository.save(reviewEntityUpdated));
    }

    @Override
    public ReviewResponseDto delete(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        reviewEntity.setStatus((byte) 0);
        ReviewEntity deletedReview = reviewRepository.save(reviewEntity);
        return reviewMapper.toResponseDto(deletedReview);
    }

    @Override
    public ReviewResponseDto toggleStatus(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        reviewEntity.setStatus(reviewEntity.getStatus() == 1 ? (byte) 0 : (byte) 1);
        ReviewEntity updatedReview = reviewRepository.save(reviewEntity);
        return reviewMapper.toResponseDto(updatedReview);
    }

    @Override
    public Iterable<ReviewResponseDto> getAllByPlan(Long idPlan) {
        TouristPlanEntity plan = touristPlanRepository.findById(idPlan)
                .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + idPlan + " not found"));
        return reviewRepository.findByTouristPlanIdAndStatus(idPlan, 1)
                .stream().map(reviewMapper::toResponseDto)
                .toList();
    }

    @Override
    public Iterable<ReviewResponseDto> getAllByRating(Long idPlan, int rating) {
        TouristPlanEntity plan = touristPlanRepository.findById(idPlan)
                .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + idPlan + " not found"));
        return reviewRepository.findByRatingAndTouristPlanIdAndStatus(rating, idPlan,1)
                .stream().map(reviewMapper::toResponseDto)
                .toList();

    }
}
