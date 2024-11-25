package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{
    List<ReviewEntity> findByTouristPlanIdAndStatus(Long idPlan, int status);
    List<ReviewEntity> findByRatingAndTouristPlanIdAndStatus(int rating, Long idPlan, int status);
    Page<ReviewEntity> findByTouristPlanIdAndStatus(Long idPlan, int status, Pageable pageable);
    Page<ReviewEntity> findByUserIdAndStatus(Long idUser, int status, Pageable pageable);
    List<ReviewEntity> findByRating(int rating);

    boolean existsByUserIdAndTouristPlanId(Long userId, Long touristPlanId);


}
