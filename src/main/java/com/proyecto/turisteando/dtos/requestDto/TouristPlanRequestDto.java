package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.entities.CharacteristicsEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class TouristPlanRequestDto implements IDto {

    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descriptgiion;

    @NotNull(message = "El precio no puede estar vacío")
    @PositiveOrZero(message = "El precio debe ser igual o mayor a cero")
    private Double price;

    // Campos  para el plan turístico con relaciones de entidades pendientes
    private String seller;

    @NotNull(message = "La ciudad no puede estar vacía")
    @Positive(message = "El id de la ciudad debe ser mayor a cero")
    private Long cityId;

    @NotNull(message = "La categoría no puede estar vacía")
    @Positive(message = "El id de la categoría debe ser mayor a cero")
    private Long categoryId;

    private List<MultipartFile> multipartImages;

    private List<String> imagesUrl;

    @NotNull(message = "La fecha de inicio de disponibilidad no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio de disponibilidad debe ser una fecha futura o presente")
    private LocalDate availabilityStartDate;

    @NotNull(message = "La fecha de fin de disponibilidad no puede estar vacía")
    @FutureOrPresent(message = "La fecha de fin de disponibilidad debe ser una fecha futura o presente")
    private LocalDate availabilityEndDate;

    @NotNull(message = "La capacidad no puede estar vacía")
    @PositiveOrZero(message = "La capacidad debe ser igual o mayor a cero")
    private Integer capacity;

    @NotEmpty(message = "La duración no puede estar vacía")
    private String duration;

    @NotEmpty(message = "Las características no pueden estar vacías")
    private List<CharacteristicsEntity> characteristic;

}
