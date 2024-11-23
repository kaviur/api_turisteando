package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.FavoriteRequestDto;
import com.proyecto.turisteando.dtos.responseDto.FavoriteResponseDto;
import com.proyecto.turisteando.entities.FavoriteEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.exceptions.customExceptions.FavoriteNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.ReservationNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.FavoriteMapper;
import com.proyecto.turisteando.repositories.FavoriteRepository;
import com.proyecto.turisteando.repositories.IUserRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.IFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements IFavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private TouristPlanRepository touristPlanRepository;

    @Autowired
    private IUserRepository userRepository;


    @Override
    public Iterable<FavoriteResponseDto> getAll() {

        Iterable<FavoriteEntity> allFavorites = favoriteRepository.findAll();
        return StreamSupport.stream(allFavorites.spliterator(), false)
                .map(favoriteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteResponseDto read(Long id) {

        return favoriteRepository.findById(id)
                .map(favoriteMapper::toDto)
                .orElseThrow(() -> new FavoriteNotFoundException("No existe un favorito con el id: " + id));
    }

    @Override
    public FavoriteResponseDto create(FavoriteRequestDto dto) {

        try {
            TouristPlanEntity touristPlan = touristPlanRepository.findById(dto.getTouristPlanId())
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan turistico con el id: " + dto.getTouristPlanId()));
                        UserEntity user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ReservationNotFoundException("No existe un usuario con el id: " + dto.getUserId()));

            System.out.println(user.toString());

            FavoriteEntity favoriteEntity = favoriteMapper.toEntity(dto);
            favoriteEntity.setTouristPlan(touristPlan);
            favoriteEntity.setUser(user);


            FavoriteEntity savedFavorites = favoriteRepository.save(favoriteEntity);
            return favoriteMapper.toDto(savedFavorites);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FavoriteResponseDto update(FavoriteRequestDto dto, Long id) {

        try{
            FavoriteEntity favorites = favoriteRepository.findById(id)
                    .orElseThrow(() -> new ReservationNotFoundException("No existe un favorito con el id: " + id));
            favoriteMapper.partialUpdate(dto, favorites);
            return favoriteMapper.toDto(favoriteRepository.save(favorites));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FavoriteResponseDto delete(Long id) {

        try {
            FavoriteEntity favorites = favoriteRepository.findById(id)
                    .orElseThrow(() -> new FavoriteNotFoundException("No existe un favorito con el id: " + id));

            favoriteRepository.delete(favorites);
            return favoriteMapper.toDto(favorites);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FavoriteResponseDto toggleStatus(Long id) {

        try {
            FavoriteEntity favorites = favoriteRepository.findById(id)
                    .orElseThrow(() -> new FavoriteNotFoundException("No existe un favorito con el id: " + id));
            favorites.setStatus(!favorites.isStatus());
            FavoriteEntity updatedFavorites = favoriteRepository.save(favorites);
            return favoriteMapper.toDto(updatedFavorites);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
