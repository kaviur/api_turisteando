package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{
    List<ReviewEntity> findByTouristPlanIdAndStatus(Long idPlan, int status);
    List<ReviewEntity> findByRatingAndTouristPlanId(int rating, Long idPlan);
    List<ReviewEntity> findByRating(int rating);


}
