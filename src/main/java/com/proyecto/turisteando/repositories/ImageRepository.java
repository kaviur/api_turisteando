package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
