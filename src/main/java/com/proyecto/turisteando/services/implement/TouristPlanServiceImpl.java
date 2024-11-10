package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.ImageEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
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

    @Override
    public Iterable<TouristPlanResponseDto> getAll() {
        Iterable<TouristPlanEntity> allTouristPlans = touristPlanRepository.findAll();
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
    public TouristPlanResponseDto read(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + id));
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public TouristPlanResponseDto create(TouristPlanRequestDto dto) {

        fileValidator.validateFiles(dto.getMultipartImages()); // Valida las imagénes y lanza una excepción de tipo FileValidationException si hay un error

        // Guarda las imágenes y obtiene las URLs
        List<String> imageUrls = fileUploadService.saveImage(dto.getMultipartImages()); // Guarda las imágenes y lanza una excepción de tipo FileUploadException si hay un error
        dto.setImagesUrl(imageUrls); // Añade las URLs al DTO como strings antes de mapear la entidad

        // Mapea el DTO a la entidad
        TouristPlanEntity touristPlanEntity = touristPlanMapper.toEntity(dto);
        final TouristPlanEntity savedTouristPlan = touristPlanRepository.save(touristPlanEntity);

        // Crear las entidades de imagen y agregarlas a la entidad del plan turístico
        List<ImageEntity> imageEntities = imageUrls.stream()
                .map(url -> ImageEntity.builder()
                        .imageUrl(url)
                        .touristPlan(savedTouristPlan)
                        .build())
                .collect(Collectors.toList());

        savedTouristPlan.setImages(imageEntities);

        return touristPlanMapper.toDto(touristPlanRepository.save(savedTouristPlan));
    }

    @Override
    public TouristPlanResponseDto update(TouristPlanRequestDto dto, Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turístico con el id: " + id));

            // Actualizar el resto de los campos del plan turístico
            touristPlanMapper.partialUpdate(dto, touristPlan);

            // Si hay imágenes en el DTO, gestionarlas
            if (dto.getMultipartImages() != null && !dto.getMultipartImages().isEmpty()) {
                // Obtener las imágenes existentes asociadas al plan turístico
                List<String> existingImages = touristPlan.getImages().stream()
                        .map(ImageEntity::getImageUrl)
                        .collect(Collectors.toList());

                // Comparar imágenes existentes con las nuevas imágenes para determinar qué imágenes eliminar
                List<String> imagesToKeep = new ArrayList<>(existingImages);  // Inicialmente, conservar todas las imágenes existentes
                List<String> imagesToDelete = new ArrayList<>();

                // Identificar las imágenes que deben eliminarse
                for (String existingImage : existingImages) {
                    if (!dto.getMultipartImages().contains(existingImage)) {
                        imagesToDelete.add(existingImage);
                        imagesToKeep.remove(existingImage); // Si se elimina, quitarla de las que se conservan
                    }
                }

                // Eliminar las entidades de imagen en la base de datos usando el método `delete` del servicio
                for (String imageUrl : imagesToDelete) {
                    // Obtener el ID de la imagen basada en la URL
                    Long imageId = touristPlan.getImages().stream()
                            .filter(img -> img.getImageUrl().equals(imageUrl))
                            .map(ImageEntity::getId)
                            .findFirst()
                            .orElseThrow(() -> new ImageNotFoundException("No existe una imagen con URL: " + imageUrl));

                    // Llamar al método delete del servicio de imágenes para eliminar la imagen de la base de datos
                    imageService.delete(imageId);
                }

                // Llamar al servicio para actualizar las imágenes, gestionando las imágenes a conservar, eliminar y las nuevas
                List<String> finalImages = fileUploadService.updateImage(imagesToKeep, imagesToDelete, dto.getMultipartImages());

                // Validar que el total de imágenes no exceda el límite de 5
                if (finalImages.size() > 5) {
                    throw new ImageLimitExceededException("No se pueden cargar más de 5 imágenes por plan turístico");
                }

                // Crear las nuevas entidades de imagen con las URLs de las imágenes finales
                List<ImageEntity> imageEntities = finalImages.stream()
                        .map(url -> ImageEntity.builder()
                                .imageUrl(url)
                                .touristPlan(touristPlan)
                                .build())
                        .collect(Collectors.toList());


                touristPlan.setImages(imageEntities);
            }

            return touristPlanMapper.toDto(touristPlanRepository.save(touristPlan));
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el plan turístico: " + e.getMessage());
        }
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
            throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException(e.getMessage());
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
                .filter(touristPlan -> iDto.getDisabilityAccess() == null ||
                        touristPlan.isDisabilityAccess() == iDto.getDisabilityAccess())
                .filter(touristPlan -> iDto.getPetsFriendly() == null ||
                        touristPlan.isPetsFriendly() == iDto.getPetsFriendly())
                .filter(touristPlan -> iDto.getFoodIncluded() == null ||
                        touristPlan.isFoodIncluded() == iDto.getFoodIncluded())
                .filter(touristPlan -> iDto.getWifiIncluded() == null ||
                        touristPlan.isWifiIncluded() == iDto.getWifiIncluded())
                .toList();
    }

}
