package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    void deleteByImageUrl(String imageUrl);

    Optional<ImageEntity> findByImageUrl(String oldImageUrl);
}
