package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TouristPlanRepository extends JpaRepository<TouristPlanEntity, Long> {
  
    boolean existsByCategoryIdAndIsActive(Long categoryId, Boolean isActive);

    @Query(value = "SELECT * FROM tourist_plan t1 INNER JOIN favorites_user_tourist_plan t2 ON t1.id = t2.tourist_plan_id WHERE t2.user_id = ?1", nativeQuery = true)
    List<TouristPlanEntity> usersFavorites (Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO favorites_user_tourist_plan (user_id, tourist_plan_id) VALUES(?1,?2)", nativeQuery = true)
    void addUsersFavorites (Long userId, Long touristPlanId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM favorites_user_tourist_plan WHERE user_id = ?1 AND tourist_plan_id = ?2", nativeQuery = true)
    void deleteUsersFavorites (Long userId, Long touristPlanId);

    List<TouristPlanEntity> findByIsActiveTrue();

    boolean existsByIdAndUsersFavorites_Id(Long planId, Long userId);

}
