package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.CategoryEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.services.implement.CategoryServiceImpl;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, CategoryServiceImpl.class})
public interface TouristPlanMapper {

    @Mapping(target = "category", source = "categoryId")
    TouristPlanEntity toEntity(TouristPlanRequestDto touristPlanRequestDto);

    TouristPlanResponseDto toDto(TouristPlanEntity touristPlanEntity);

    List<TouristPlanResponseDto> toDtoList(List<TouristPlanEntity> touristPlanEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TouristPlanEntity partialUpdate(TouristPlanRequestDto touristPlanRequestDto, @MappingTarget TouristPlanEntity touristPlanEntity1);

    default CategoryEntity getCategoryById(Long categoryId, @Context CategoryServiceImpl categoryService) {
        return categoryService.readEntity(categoryId);
    }
}
