package com.proyecto.turisteando.dtos.requestDto;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.entities.CategoryEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TouristPlanRequestDto implements IDto {

    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotNull(message = "El precio no puede estar vacío")
    @PositiveOrZero(message = "El precio debe ser igual o mayor a cero")
    private Double price;

    // Campos  para el plan turístico con relaciones de entidades pendientes
    private String seller;
    private String city;

    @NotNull(message = "La categoría no puede estar vacía")
    private Long categoryId;


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

    private Boolean foodIncluded;
    private Boolean wifiIncluded;
    private Boolean petsFriendly;
    private Boolean disabilityAccess;

}
