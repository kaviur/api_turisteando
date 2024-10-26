package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.TouristPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristPlanRepository extends JpaRepository<TouristPlanEntity, Long> {
}
