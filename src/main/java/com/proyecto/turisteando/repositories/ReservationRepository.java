package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository <ReservationEntity, Long> {

    // Encontrar reservas por estado
   //List<ReservationEntity> findByStatus(String status);

    // Encontrar reservas activas por plan turístico
    //@Query("SELECT r FROM ReservationEntity r WHERE r.touristPlan.id = :planId AND r.status = 'Confirmed'")
    //List<ReservationEntity> findConfirmedReservationsByPlanId(@Param("planId") Long planId);
}


