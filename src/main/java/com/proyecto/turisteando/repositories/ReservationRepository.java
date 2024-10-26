package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository <ReservationEntity, Long> {

}
