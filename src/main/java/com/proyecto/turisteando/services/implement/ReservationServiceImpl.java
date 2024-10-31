package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;
import com.proyecto.turisteando.entities.ReservationEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.exceptions.customExceptions.ReservationNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.ReservationMapper;
import com.proyecto.turisteando.repositories.ReservationRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.IReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements IReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private TouristPlanRepository touristPlanRepository;

    @Override
    public Iterable<ReservationResponseDto> getAll() {
        Iterable<ReservationEntity> allReservations = reservationRepository.findAll();
        return StreamSupport.stream(allReservations.spliterator(), false)
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponseDto read(Long id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toDto)
                .orElseThrow(() -> new ReservationNotFoundException("No existe una reserva con el id: " + id));
    }

    @Override
    public ReservationResponseDto create(ReservationRequestDto dto) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(dto.getTouristPlanId())
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + dto.getTouristPlanId()));

            ReservationEntity reservationEntity = reservationMapper.toEntity(dto);
            reservationEntity.setTouristPlan(touristPlan);

            ReservationEntity savedReservation = reservationRepository.save(reservationEntity);
            return reservationMapper.toDto(savedReservation);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
    }

    @Override
    public ReservationResponseDto update(ReservationRequestDto dto, Long id) {
        try{
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No existe una reserva con el id: " + id));
        // Actualizar campos de la entidad
        reservationMapper.partialUpdate(dto, reservation);
        return reservationMapper.toDto(reservationRepository.save(reservation));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ReservationResponseDto delete(Long id) {
        try {
            ReservationEntity reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationNotFoundException("No existe una reserva con el id: " + id));

            reservationRepository.delete(reservation);
            return reservationMapper.toDto(reservation);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ReservationResponseDto toggleStatus(Long id) {
        try {
            ReservationEntity reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationNotFoundException("No existe una reserva con el id: " + id));
            // Alternar el estado entre Confirmed y Cancelled
            reservation.setStatus(!reservation.isStatus());
            ReservationEntity updatedReservation = reservationRepository.save(reservation);
            return reservationMapper.toDto(updatedReservation);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
