package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.dtos.requestDto.CategoryRequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TouristPlanResponseDto implements IDto {

    private Long id;
    private String title;
    private String description;
    private Double price;

    // Campos  para el plan turístico con relaciones de entidades pendientes
    private String seller;

    private CityResponseDto city;
    private CategoryResponseDto category;
    private List<ImageResponseDto> images;
    private LocalDate availabilityStartDate;
    private LocalDate availabilityEndDate;
    private int capacity;
    private String duration;
    private List<CharacteristicResponseDto> characteristic;
    private List<ReviewResponseDto> reviews;
    private Integer totalReviews;
    private Integer totalStars;
    private Double rating; // Este será calculado en el mapper
    private boolean isActive;
}
