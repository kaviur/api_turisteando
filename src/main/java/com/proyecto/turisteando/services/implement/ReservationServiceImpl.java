package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.ReservationDto;
import com.proyecto.turisteando.entities.ReservationEntity;
import com.proyecto.turisteando.mappers.ReservationMapper;
import com.proyecto.turisteando.repositories.ReservationRepository;
import com.proyecto.turisteando.services.ICrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ICrudService<ReservationDto, Long> {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;


    @Override
    public Iterable<ReservationDto> getAll() {
        List<ReservationEntity> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @Override
    public ReservationDto read(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return reservationMapper.toDto(reservation);
    }

    @Override
    public ReservationDto create(ReservationDto dto) {
        ReservationEntity reservationEntity = reservationMapper.toEntity(dto);
        ReservationEntity savedReservation = reservationRepository.save(reservationEntity);
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public ReservationDto update(ReservationDto dto, Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        // Actualizar campos de la entidad
        reservation.setStatus(dto.getStatus());
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        // Agrega cualquier otro campo que necesites actualizar
        ReservationEntity updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(updatedReservation);
    }

    @Override
    public ReservationDto delete(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reservationRepository.delete(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    public ReservationDto toggleStatus(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        // Alternar estado
        if ("Confirmed".equals(reservation.getStatus())) {
            reservation.setStatus("Cancelled");
        } else {
            reservation.setStatus("Confirmed");
        }

        ReservationEntity updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(updatedReservation);
    }
}
