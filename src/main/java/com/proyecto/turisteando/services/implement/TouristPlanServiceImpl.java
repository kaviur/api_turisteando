package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.ImageEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.exceptions.customExceptions.FileValidationException;
import com.proyecto.turisteando.exceptions.customExceptions.ImageLimitExceededException;
import com.proyecto.turisteando.exceptions.customExceptions.ImageNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.TouristPlanMapper;
import com.proyecto.turisteando.repositories.ImageRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.FileUploadService;
import com.proyecto.turisteando.services.IImageService;
import com.proyecto.turisteando.services.ITouristPlanService;
import com.proyecto.turisteando.utils.FileValidator;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TouristPlanServiceImpl implements ITouristPlanService {

    @Autowired
    private TouristPlanRepository touristPlanRepository;

    @Autowired
    private TouristPlanMapper touristPlanMapper;

    @Autowired
    private FileValidator fileValidator;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private ImageRepository imageRepository;


    @Override
    public Iterable<TouristPlanResponseDto> getAll() {
        Iterable<TouristPlanEntity> allTouristPlans = touristPlanRepository.findByIsActiveTrue();
        return StreamSupport.stream(allTouristPlans.spliterator(), false)
                .map(touristPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<TouristPlanResponseDto> getAllByFilters(TouristPlanRequestDto iDto) {
        List<TouristPlanEntity> allTouristPlans = touristPlanRepository.findAll();
        List<TouristPlanEntity> filteredTouristPlans = filterTouristPlans(allTouristPlans, (TouristPlanRequestDto) iDto);
        return filteredTouristPlans.stream()
                .map(touristPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TouristPlanResponseDto> findAllFavoritesByUserId(Long userId) {
        return touristPlanMapper.toDtoList(touristPlanRepository.usersFavorites(userId));
    }

    @Override
    public void addUsersFavorites(Long userId, Long touristPlanId) {
        boolean exists = touristPlanRepository.existsByIdAndUsersFavorites_Id(touristPlanId, userId);
        if (exists) {
            throw new ServiceException("El usuario ya tiene este plan turístico como favorito.");
        }
        touristPlanRepository.addUsersFavorites(userId, touristPlanId);
    }

    @Override
    public void deleteUsersFavorites(Long userId, Long touristPlanId) {
        touristPlanRepository.deleteUsersFavorites(userId, touristPlanId);
    }

    @Override
    public TouristPlanResponseDto read(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + id));
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new ServiceException("Error al leer el plan turistico: " + e.getMessage());
        }
    }


    @Override
    public TouristPlanResponseDto create(TouristPlanRequestDto dto) {

        fileValidator.validateFiles(dto.getMultipartImages());

        List<String> imageUrls = fileUploadService.saveImage(dto.getMultipartImages()); // Guarda las imágenes y lanza una excepción de tipo FileUploadException si hay un error
        dto.setImagesUrl(imageUrls);

        TouristPlanEntity touristPlanEntity = touristPlanMapper.toEntity(dto);

        List<ImageEntity> imageEntities = imageUrls.stream()
                .map(url -> ImageEntity.builder()
                        .imageUrl(url)
                        .touristPlan(touristPlanEntity)
                        .build())
                .collect(Collectors.toList());
        touristPlanEntity.setImages(imageEntities);

        TouristPlanEntity savedTouristPlan = touristPlanRepository.save(touristPlanEntity);

        return touristPlanMapper.toDto(savedTouristPlan);
    }

    @Override
    public TouristPlanResponseDto update(TouristPlanRequestDto dto, Long id) {

        TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turístico con el id: " + id));

        // Actualizar el resto de los campos del plan turístico
        touristPlanMapper.partialUpdate(dto, touristPlan);

        // Obtener imágenes a eliminar y las nuevas imágenes
        List<String> imagesToDelete = dto.getImagesToDelete();
        List<MultipartFile> newImages = dto.getMultipartImages();

        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            if (newImages == null || newImages.size() != imagesToDelete.size()) {
                throw new FileValidationException("La cantidad de imágenes nuevas debe coincidir con las imágenes a eliminar.");
            }

            fileValidator.validateFiles(newImages);
            // Subir nuevas imágenes a Cloudinary
            List<String> newImageUrls = fileUploadService.saveImage(newImages);
            dto.setImagesUrl(newImageUrls);

            // Reemplazar imágenes en la base de datos
            for (int i = 0; i < imagesToDelete.size(); i++) {
                String oldImageUrl = imagesToDelete.get(i);
                String newImageUrl = newImageUrls.get(i);

                // Buscar la entidad de imagen en la base de datos
                ImageEntity imageEntity = imageRepository.findByImageUrl(oldImageUrl)
                        .orElseThrow(() -> new ImageNotFoundException("La imagen con URL: " + oldImageUrl + " no existe."));

                // Actualizar la URL en la entidad
                imageEntity.setImageUrl(newImageUrl);
                imageRepository.save(imageEntity);
            }

            // Eliminar las imágenes antiguas de Cloudinary
            fileUploadService.deleteExistingImages(imagesToDelete);
        }

        // Validar que el total de imágenes no exceda el límite de 5
        if (touristPlan.getImages().size() > 5) {
            throw new ImageLimitExceededException("No se pueden cargar más de 5 imágenes por plan turístico");
        }

        return touristPlanMapper.toDto(touristPlanRepository.save(touristPlan));

    }


    @Override
    public TouristPlanResponseDto delete(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + id));
            touristPlan.setActive(false);
            touristPlanRepository.save(touristPlan);
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public TouristPlanResponseDto toggleStatus(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + id));
            touristPlan.setActive(!touristPlan.isActive());
            touristPlanRepository.save(touristPlan);
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private List<TouristPlanEntity> filterTouristPlans(List<TouristPlanEntity> allTouristPlans, TouristPlanRequestDto iDto) {
        return allTouristPlans.stream()
                .filter(touristPlan -> iDto.getCategoryId() == null ||
                        Objects.equals(touristPlan.getCategory().getId(), iDto.getCategoryId()))
                .filter(touristPlan -> iDto.getTitle() == null ||
                        touristPlan.getTitle().toLowerCase().contains(iDto.getTitle().toLowerCase()))
                .filter(touristPlan -> iDto.getDescription() == null ||
                        touristPlan.getDescription().toLowerCase().contains(iDto.getDescription().toLowerCase()))
                .filter(touristPlan -> iDto.getCityId() == null ||
                        touristPlan.getCity().getId().equals(iDto.getCityId()))
                .filter(touristPlan -> iDto.getCapacity() == null ||
                        touristPlan.getCapacity().equals(iDto.getCapacity()))
                .filter(touristPlan -> iDto.getPrice() == null ||
                        touristPlan.getPrice().equals(iDto.getPrice()))
                .filter(touristPlan -> iDto.getDuration() == null ||
                        touristPlan.getDuration().equals(iDto.getDuration()))
                .filter(touristPlan -> iDto.getSeller() == null ||
                        touristPlan.getSeller().equals(iDto.getSeller()))
                .filter(touristPlan -> iDto.getAvailabilityStartDate() == null ||
                        touristPlan.getAvailabilityEndDate().isEqual(iDto.getAvailabilityStartDate()))
                .filter(touristPlan -> iDto.getAvailabilityEndDate() == null ||
                        touristPlan.getAvailabilityEndDate().isEqual(iDto.getAvailabilityEndDate()))
                .toList();
    }

}
