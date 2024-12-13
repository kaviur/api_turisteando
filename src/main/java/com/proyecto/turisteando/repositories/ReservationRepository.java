package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ReservationRepository extends JpaRepository <ReservationEntity, Long> {

    List<ReservationEntity> findByTouristPlanId(Long touristPlanId);
    List<ReservationEntity> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<ReservationEntity> findByUserIdAndStatus(Long userId, boolean status);
    boolean existsByUserIdAndTouristPlanId(Long userId, Long touristPlanId);




}


