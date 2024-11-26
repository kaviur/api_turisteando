package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;
import com.proyecto.turisteando.entities.ReservationEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.exceptions.customExceptions.ReservationNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.ReservationMapper;
import com.proyecto.turisteando.repositories.IUserRepository;
import com.proyecto.turisteando.repositories.ReservationRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.IReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Autowired
    private IUserRepository userRepository;

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
                // Validar que las fechas de la reserva estén dentro del rango de disponibilidad del plan turístico
                if (dto.getStartDate().isBefore(touristPlan.getAvailabilityStartDate()) ||
                    dto.getEndDate().isAfter(touristPlan.getAvailabilityEndDate()) ) {
                throw new ReservationNotFoundException("La fecha de la reserva debe estar entre las fechas de disponibilidad del plan turístico y la fecha de inicio no puede ser después de la fecha de fin.");
            }

            UserEntity user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ReservationNotFoundException("No existe un usuario con el id: " + dto.getUserId()));

            System.out.println(user.toString());

            ReservationEntity reservationEntity = reservationMapper.toEntity(dto);
            reservationEntity.setTouristPlan(touristPlan);
            reservationEntity.setUser(user);


            ReservationEntity savedReservation = reservationRepository.save(reservationEntity);
            return reservationMapper.toDto(savedReservation);
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
    }

    @Override
    public ReservationResponseDto update(ReservationRequestDto dto, Long id) {
        try{
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No existe una reserva con el id: " + id));
        reservationMapper.partialUpdate(dto, reservation);
        return reservationMapper.toDto(reservationRepository.save(reservation));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
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
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ReservationResponseDto toggleStatus(Long id) {
        try {
            ReservationEntity reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationNotFoundException("No existe una reserva con el id: " + id));
            reservation.setStatus(!reservation.isStatus());
            ReservationEntity updatedReservation = reservationRepository.save(reservation);
            return reservationMapper.toDto(updatedReservation);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Iterable<ReservationResponseDto> getReservationsByTouristPlan(Long touristPlanId) {
        List<ReservationEntity> reservations = reservationRepository.findByTouristPlanId(touristPlanId);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No se encontraron reservas para el plan turístico con ID: " + touristPlanId);
        }
        return reservations.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<ReservationResponseDto> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ReservationNotFoundException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        List<ReservationEntity> reservations = reservationRepository.findByStartDateBetween(startDate, endDate);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No se encontraron reservas en el rango de fechas especificado.");
        }
        return reservations.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<ReservationResponseDto> findByUserIdAndStatus(Long userId, boolean status) {
        List<ReservationEntity> reservations = reservationRepository.findByUserIdAndStatus(userId, status);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No se encontraron reservas para el usuario con ID: " + userId);
        }
        return reservations.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }


}
