package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.CityRequestDto;
import com.proyecto.turisteando.dtos.responseDto.CityResponseDto;
import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.entities.CountryEntity;
import com.proyecto.turisteando.services.implement.CountryServiceImpl;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {CountryMapper.class, CountryServiceImpl.class})
public interface CityMapper {

    @Mapping(target = "country", source = "countryId")
    CityEntity toEntity(CityRequestDto cityRequestDto);

    CityResponseDto toDto(CityEntity cityEntity);

    List<CityResponseDto> toDtoList(List<CityEntity> cityEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CityEntity partialUpdate(CityRequestDto cityRequestDto, @MappingTarget CityEntity cityEntity);

    default CountryEntity getCountryById(Long countryId, @Context CountryServiceImpl countryService) {
        return countryService.getCountry(countryId);
    }

}
