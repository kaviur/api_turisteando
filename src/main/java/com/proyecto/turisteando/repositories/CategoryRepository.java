package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    /**
     * Finds all categories that are active.
     *
     * @param i Status of the category. 1 means active, 0 means inactive.
     * @return List of active categories.
     */
    List<CategoryEntity> findByStatus(byte i);

    /**
     * Finds a category by its ID and status.
     *
     * @param id The ID of the category.
     * @param i  The status of the category. 1 means active, 0 means inactive.
     * @return An Optional containing the CategoryEntity if found, otherwise an empty Optional.
     */
    Optional<CategoryEntity> findByIdAndStatus(Long id, int i);
}
