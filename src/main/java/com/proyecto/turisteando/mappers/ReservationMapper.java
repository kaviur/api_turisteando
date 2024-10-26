package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.ReservationDto;
import com.proyecto.turisteando.entities.ReservationEntity;
import org.mapstruct.*;



@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    ReservationEntity toEntity(ReservationDto reservationDto);
    ReservationDto toDto(ReservationEntity reservationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReservationEntity partialUpdate(ReservationDto reservationDto, @MappingTarget ReservationEntity reservationEntity);
}
