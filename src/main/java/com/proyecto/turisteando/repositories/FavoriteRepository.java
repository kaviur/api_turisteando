package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
}
