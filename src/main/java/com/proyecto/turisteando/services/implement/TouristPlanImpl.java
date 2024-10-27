package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.TouristPlanMapper;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.ICrudService;
import com.proyecto.turisteando.services.ITouristPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class TouristPlanImpl implements ITouristPlanService {

    @Autowired
    private TouristPlanRepository touristPlanRepository;

    @Autowired
    private TouristPlanMapper touristPlanMapper;

    @Override
    public Iterable<IDto> getAll() {
        Iterable<TouristPlanEntity> allTouristPlans = touristPlanRepository.findAll();
        return StreamSupport.stream(allTouristPlans.spliterator(), false)
                .map(touristPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<IDto> getAllByFilters(IDto iDto) {
        List<TouristPlanEntity> allTouristPlans = touristPlanRepository.findAll();
        List<TouristPlanEntity> filteredTouristPlans = filterTouristPlans(allTouristPlans, (TouristPlanRequestDto) iDto);
        return filteredTouristPlans.stream()
                .map(touristPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public IDto read(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(id)
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + id));
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public IDto create(IDto dto) {
        try {
            TouristPlanEntity touristPlan = touristPlanRepository
                    .save(touristPlanMapper.toEntity((TouristPlanRequestDto) dto));
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public IDto update(IDto dto, Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanMapper.toEntity((TouristPlanRequestDto) read(id));
            touristPlanMapper.partialUpdate((TouristPlanRequestDto) dto, touristPlan);
            return touristPlanMapper.toDto(touristPlanRepository.save(touristPlan));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public IDto delete(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanMapper.toEntity((TouristPlanRequestDto) read(id));
            touristPlan.setActive(false);
            touristPlanRepository.save(touristPlan);
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public IDto toggleStatus(Long id) {
        try {
            TouristPlanEntity touristPlan = touristPlanMapper.toEntity((TouristPlanRequestDto) read(id));
            touristPlan.setActive(!touristPlan.isActive());
            touristPlanRepository.save(touristPlan);
            return touristPlanMapper.toDto(touristPlan);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<TouristPlanEntity> filterTouristPlans(List<TouristPlanEntity> allTouristPlans, TouristPlanRequestDto iDto) {
        return allTouristPlans.stream()
                .filter(touristPlan -> iDto.getCategoryId() == null ||
                        Objects.equals(touristPlan.getCategory().getId(), iDto.getCategoryId()))
                .filter(touristPlan -> iDto.getTitle() == null ||
                        touristPlan.getTitle().toLowerCase().contains(iDto.getTitle().toLowerCase()))
                .filter(touristPlan -> iDto.getDescription() == null ||
                        touristPlan.getDescription().toLowerCase().contains(iDto.getDescription().toLowerCase()))
                .filter(touristPlan -> iDto.getCity() == null ||
                        touristPlan.getCity().toLowerCase().contains(iDto.getCity()))
                .filter(touristPlan -> iDto.getCapacity() == null ||
                        touristPlan.getCapacity().equals(iDto.getCapacity()))
                .filter(touristPlan -> iDto.getPrice() == null ||
                        touristPlan.getPrice().equals(iDto.getPrice()))
                .filter(touristPlan -> iDto.getDuration() == null ||
                        touristPlan.getDuration().equals(iDto.getDuration()))
                .filter(touristPlan -> iDto.getSeller() == null ||
                        touristPlan.getSeller().equals(iDto.getSeller()))
                .filter(touristPlan -> iDto.getAvailabilityStartDate() == null ||
                        touristPlan.getAvailabilityEndDate().isEqual(iDto.getAvailabilityStartDate()))
                .filter(touristPlan -> iDto.getAvailabilityEndDate() == null ||
                        touristPlan.getAvailabilityEndDate().isEqual(iDto.getAvailabilityEndDate()))
                .filter(touristPlan -> iDto.getDisabilityAccess() == null ||
                        touristPlan.isDisabilityAccess() == iDto.getDisabilityAccess())
                .filter(touristPlan -> iDto.getPetsFriendly() == null ||
                        touristPlan.isPetsFriendly() == iDto.getPetsFriendly())
                .filter(touristPlan -> iDto.getFoodIncluded() == null ||
                        touristPlan.isFoodIncluded() == iDto.getFoodIncluded())
                .filter(touristPlan -> iDto.getWifiIncluded() == null ||
                        touristPlan.isWifiIncluded() == iDto.getWifiIncluded())
                .toList();
    }

}
