package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.CategoryRequestDto;
import com.proyecto.turisteando.entities.CategoryEntity;
import com.proyecto.turisteando.entities.ImageEntity;
import com.proyecto.turisteando.exceptions.customExceptions.CategoryNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.UnauthorizedActionException;
import com.proyecto.turisteando.mappers.CategoryMapper;
import com.proyecto.turisteando.repositories.CategoryRepository;
import com.proyecto.turisteando.repositories.ImageRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.FileUploadService;
import com.proyecto.turisteando.services.ICategoryService;
import com.proyecto.turisteando.services.ICrudService;
import com.proyecto.turisteando.utils.FileValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Implementation of the CategoryService interface, which provides methods
 * for accessing and managing category-related information.
 *
 * @author Karen Urbano - <a href="https://github.com/kaviur">kaviur</a>
 * @version 1.0
 * @since 2024-10-25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileValidator fileValidator;
    private final FileUploadService fileUploadService;
    private final ImageRepository imageRepository;
    private final TouristPlanRepository touristPlanRepository;

    /**
     * Retrieves all available categories.
     * If the user is an administrator, all categories are returned.
     * If the user is not an administrator, only categories with an active status are considered available.
     *
     * @return An iterable collection of category DTOs representing the available categories.
     */

    @Override
    public Iterable<IDto> getAll() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = auth.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//
//        if (isAdmin){
//            return categoryRepository.findAll().stream()
//                    .map(categoryMapper::toDto)
//                    .toList();
//        }

        Iterable<CategoryEntity> categories = categoryRepository.findByStatus((byte) 1);

        return StreamSupport.stream(categories.spliterator(), false)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }


    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return A DTO of the category if found, otherwise an exception is thrown.
     */
    @Override
    public IDto read(Long id) {
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
        return categoryMapper.toDto(categoryRepository.findById(id).orElse(null));
//        return null;
    }

    public CategoryEntity readEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una categoría con el id " + id));
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
    public IDto create(IDto dto) {
        CategoryRequestDto categoryDto = (CategoryRequestDto) dto;

        try {
            // Validar la imagen
            fileValidator.validateFiles(Collections.singletonList(categoryDto.getImage()));

            // Subir la imagen y obtener la URL
            String imageUrl = fileUploadService.saveImage(Collections.singletonList(categoryDto.getImage())).get(0);

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageUrl(imageUrl);

            // Mapear el DTO a la entidad y asignar la imagen
            CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);
            categoryEntity.setImage(imageEntity);

            CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

            return categoryMapper.toDto(savedCategory);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("name")) {
                throw new ServiceException("Ya existe la categoría", e);
            }
            throw new ServiceException("Error al crear la categoría: " + e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Error al crear la categoría: " + e.getMessage());
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
    public IDto update(IDto dto, Long id) {
        CategoryRequestDto categoryDto = (CategoryRequestDto) dto;

        // Buscar la categoría existente
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("No se encontró la categoría"));

        if (categoryDto.getImage() != null && !categoryDto.getImage().isEmpty()) {
            // Validar y subir la nueva imagen en cloudinary
            fileValidator.validateFiles(Collections.singletonList(categoryDto.getImage()));
            List<String> imageUrls = fileUploadService.saveImage(Collections.singletonList(categoryDto.getImage()));
            String newImageUrl = imageUrls.get(0);

            // Actualizar los datos de la imagen existente
            ImageEntity existingImage = category.getImage();
            if (existingImage != null) {
                // Eliminar el archivo anterior de Cloudinary
                fileUploadService.deleteExistingImages(Collections.singletonList(category.getImage().getImageUrl()));

                existingImage.setImageUrl(newImageUrl);
                imageRepository.save(existingImage); // Guardar cambios en la imagen existente
            } else {
                // Si no hay imagen existente, crear una nueva
                ImageEntity newImage = new ImageEntity();
                newImage.setImageUrl(newImageUrl);
                imageRepository.save(newImage);
                category.setImage(newImage);
            }
        }

        // Actualizar los demás campos de la categoría con el DTO recibido
        categoryMapper.partialUpdate(categoryDto, category);

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
    public IDto delete(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findByIdAndStatus(id, 1)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la categorá a eliminar"));

        // Verificar si hay productos asociados a esta categoría
        boolean hasActiveProducts = touristPlanRepository.existsByCategoryIdAndIsActive(id, true);
        if (hasActiveProducts) {
            throw new UnauthorizedActionException("No se puede eliminar la categoría porque tiene productos asociados.");
        }

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
    //este no tiene la validación para que no se pueda desactivar una categoría con planes turísticos ya que podría ser una acción para un super admin
    @Override
    public IDto toggleStatus(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la categoría"));

        byte newStatus = (categoryEntity.getStatus() == 1) ? (byte) 0 : (byte) 1;
        categoryEntity.setStatus(newStatus);

        categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(categoryEntity);
    }
}