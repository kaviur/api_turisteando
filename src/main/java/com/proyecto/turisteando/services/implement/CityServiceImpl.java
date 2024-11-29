package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.CityRequestDto;
import com.proyecto.turisteando.dtos.responseDto.CityResponseDto;
import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.exceptions.customExceptions.CityNotFoundException;
import com.proyecto.turisteando.mappers.CityMapper;
import com.proyecto.turisteando.repositories.CityRepository;
import com.proyecto.turisteando.services.CrudService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
public class CityServiceImpl implements CrudService<CityRequestDto, CityResponseDto, Long> {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Override
    public Iterable<CityResponseDto> getAll() {
        try {
            Iterable<CityEntity> cities = cityRepository.findAll();
            return StreamSupport.stream(cities.spliterator(), false)
                    .map(cityMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CityResponseDto read(Long id) {
        try {
            CityEntity city = cityRepository.findById(id)
                    .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
            return cityMapper.toDto(city);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public CityEntity getEntity(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
    }

    @Override
    public CityResponseDto create(CityRequestDto dto) {
        try {
            CityEntity city = cityMapper.toEntity(dto);
            return cityMapper.toDto(cityRepository.save(city));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CityResponseDto update(CityRequestDto dto, Long id) {
        try {
            CityEntity city = cityRepository.findById(id)
                    .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
            cityMapper.partialUpdate(dto, city);
            return cityMapper.toDto(cityRepository.save(city));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CityResponseDto delete(Long id) {
        try {
            CityEntity city = cityRepository.findById(id)
                    .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
            cityRepository.delete(city);
            return cityMapper.toDto(city);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CityResponseDto toggleStatus(Long id) {
        return null;
    }

    @Override
    public CityResponseDto toggleUserRole(Long id) {
        return null;
    }
}
