package com.proyecto.turisteando.mappers;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;
import com.proyecto.turisteando.entities.ReservationEntity;
import org.mapstruct.*;



@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface ReservationMapper {
    ReservationEntity toEntity(ReservationRequestDto reservationRequestDto);

    @Mapping(target = "touristPlanId", source = "touristPlan.id")
    ReservationResponseDto toDto(ReservationEntity reservationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReservationEntity partialUpdate(ReservationRequestDto reservationRequestDto, @MappingTarget ReservationEntity reservationEntity);
}

