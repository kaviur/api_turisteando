package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.CategoryEntity;
import com.proyecto.turisteando.entities.CharacteristicEntity;
import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.services.implement.CategoryServiceImpl;
import com.proyecto.turisteando.services.implement.CharacteristicServiceImpl;
import com.proyecto.turisteando.services.implement.CityServiceImpl;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        uses = {CategoryMapper.class, CategoryServiceImpl.class, CityMapper.class, CityServiceImpl.class,
                ImageMapper.class, CharacteristicMapper.class, CharacteristicServiceImpl.class})
public interface TouristPlanMapper {

    @Mappings({
            @Mapping(target = "city", source = "cityId"),
            @Mapping(target = "category", source = "categoryId"),
            @Mapping(target = "characteristic", source = "characteristicIds")
    })
    TouristPlanEntity toEntity(TouristPlanRequestDto touristPlanRequestDto);

    @Mapping(target = "rating", expression = "java(calculateRating(touristPlan))")
    TouristPlanResponseDto toDto(TouristPlanEntity touristPlanEntity);

    List<TouristPlanResponseDto> toDtoList(List<TouristPlanEntity> touristPlanEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "city", source = "cityId"),
            @Mapping(target = "category", source = "categoryId")
            ,@Mapping(target = "characteristic", source = "characteristicIds")
    })
    TouristPlanEntity partialUpdate(TouristPlanRequestDto touristPlanRequestDto, @MappingTarget TouristPlanEntity touristPlanEntity1);

    default CategoryEntity getCategoryById(Long categoryId, @Context CategoryServiceImpl categoryService) {
        return categoryService.readEntity(categoryId);
    }

    default CityEntity getCityById(Long cityId, @Context CityServiceImpl cityService) {
        return cityService.getEntity(cityId);
    }

    default List<CharacteristicEntity> getCharacteristicByIds(List<Long> characteristicIds,
                                                              @Context CharacteristicServiceImpl characteristicService) {
        return characteristicService.getCharacteristicsByIds(characteristicIds);
    }

    // Método para calcular el rating
    default Double calculateRating(TouristPlanEntity touristPlan) {
        if (touristPlan.getTotalReviews() == 0) {
            return 0.0;
        }
        return (double) touristPlan.getTotalStars() / touristPlan.getTotalReviews();
    }

}
