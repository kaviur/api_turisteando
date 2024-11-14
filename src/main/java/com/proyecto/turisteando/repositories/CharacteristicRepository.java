package com.proyecto.turisteando.repositories;


import com.proyecto.turisteando.entities.CharacteristicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacteristicRepository extends JpaRepository<CharacteristicEntity, Long> {
    /**
     * Finds all categories that are active.
     *
     * @param i Status of the category. 1 means active, 0 means inactive.
     * @return List of active categories.
     */
    List<CharacteristicEntity> findByStatus(byte i);

    /**
     * Finds a category by its ID and status.
     *
     * @param id The ID of the category.
     * @param i  The status of the category. 1 means active, 0 means inactive.
     * @return An Optional containing the CategoryEntity if found, otherwise an empty Optional.
     */
    Optional<CharacteristicEntity> findByIdAndStatus(Long id, int i);
}
