package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.CountryDto;
import com.proyecto.turisteando.dtos.requestDto.CountryRequestDto;
import com.proyecto.turisteando.entities.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CountryMapper {

    CountryDto toDto(CountryEntity countryEntity);

    CountryEntity toEntity(CountryDto countryDto);

}
