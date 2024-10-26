package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.CategoryDto;
import com.proyecto.turisteando.entities.CategoryEntity;
import com.proyecto.turisteando.exceptions.customExceptions.CategoryNotFoundException;
import com.proyecto.turisteando.mappers.CategoryMapper;
import com.proyecto.turisteando.repositories.CategoryRepository;
import com.proyecto.turisteando.services.ICrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Implementation of the CategoryService interface, which provides methods
 * for accessing and managing category-related information.
 * @author Karen Urbano - <a href="https://github.com/kaviur">kaviur</a>
 * @version 1.0
 * @since 2024-10-25
 * */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements ICrudService<CategoryDto, Long> {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves all available categories.
     * If the user is an administrator, all categories are returned.
     * If the user is not an administrator, only categories with an active status are considered available.
     *
     * @return An iterable collection of category DTOs representing the available categories.
     */

    @Override
    public Iterable<CategoryDto> getAll() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = auth.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//
//        if (isAdmin){
//            return categoryRepository.findAll().stream()
//                    .map(categoryMapper::toDto)
//                    .toList();
//        }

        return categoryRepository.findByStatus((byte) 1).stream()
                .map(categoryMapper::toDto)
                .toList();

    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return A DTO of the category if found, otherwise an exception is thrown.
     */
    @Override
    public CategoryDto read(Long id) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = auth.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//
//        CategoryEntity categoryEntity;
//
//        if (!isAdmin){
//            categoryEntity = categoryRepository.findByIdAndStatus(id, 1)
//                    .orElseThrow(() -> new CategoryNotFoundException("No se encontró la categoría"));
//        }else{
//            categoryEntity = categoryRepository.findById(id)
//                    .orElseThrow(() -> new CategoryNotFoundException("No se encontró la categoría"));
//        }
//
//        return categoryMapper.toDto(categoryEntity);
        return null;
    }

    /**
     * Creates a new category using the data provided in the DTO object.
     * If creation is successful, returns the DTO of the newly created category.
     * If an error occurs due to a unique key constraint violation,
     * a ServiceException is thrown with a message indicating that a category
     * with the same name already exists.
     * If any other error occurs during creation, a ServiceException is thrown
     * with a generic error message.
     *
     * @param dto The DTO containing the data of the category to create.
     * @return The DTO of the newly created category.
     * @throws ServiceException If an error occurs during category creation.
     */
    @Override
    public CategoryDto create(CategoryDto dto) {
        try {
            CategoryEntity categoryEntity = categoryRepository.save(categoryMapper.toEntity(dto));
            return categoryMapper.toDto(categoryEntity);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("name")) {
                throw new ServiceException("Ya existe la categoría", e);
            }
            throw new ServiceException("Error al crear la categoría", e);
        } catch (Exception e) {
            throw new ServiceException("Error al crear la categoría: ", e);
        }
    }

    /**
     * Updates an existing category with the data provided in the DTO object.
     * If the update is successful, returns the DTO of the updated category.
     * If the specified category does not exist, a ServiceException is thrown
     * with a message indicating that the category was not found.
     * If an error occurs during the update, a ServiceException is thrown
     * with a generic error message.
     *
     * @param dto The DTO containing the updated data of the category.
     * @param id  The ID of the category to update.
     * @return The DTO of the updated category.
     * @throws ServiceException If the specified category is not found or an error occurs during category update.
     */
    @Override
    public CategoryDto update(CategoryDto dto, Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("No se encontró la categoría"));

        categoryMapper.partialUpdate(dto, category);
        CategoryEntity updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }


    // for security reasons, an administrator can only disable a category by changing its status, but the category is not deleted from the database.

    /**
     * Disables a category by changing its status to 0.
     * If the category is successfully disabled, returns the DTO of the disabled category.
     *
     * @param id The ID of the category to disable.
     * @return The DTO of the disabled category if found, or an exception otherwise.
     */
    @Override
    public CategoryDto delete(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findByIdAndStatus(id, 1)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la categorá a eliminar"));

        categoryEntity.setStatus((byte) 0);
        categoryRepository.save(categoryEntity);

        return categoryMapper.toDto(categoryEntity);
    }

    /**
     * Toggles the status of a category between active and inactive.
     * If the category is active, it is disabled by changing its status to 0.
     * If the category is inactive, it is enabled by changing its status to 1.
     *
     * @param id The ID of the category to toggle its status.
     * @return The DTO of the category with the updated status.
     */
    @Override
    public CategoryDto toggleStatus(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la categoría"));

        byte newStatus = (categoryEntity.getStatus() == 1) ? (byte) 0 : (byte) 1;
        categoryEntity.setStatus(newStatus);

        categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(categoryEntity);
    }
}