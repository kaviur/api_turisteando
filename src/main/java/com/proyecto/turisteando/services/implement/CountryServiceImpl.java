package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.CountryDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;
import com.proyecto.turisteando.entities.CountryEntity;
import com.proyecto.turisteando.exceptions.customExceptions.CountryNotFoundException;
import com.proyecto.turisteando.mappers.CountryMapper;
import com.proyecto.turisteando.repositories.CountryRepository;
import com.proyecto.turisteando.services.CrudService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
public class CountryServiceImpl implements CrudService<CountryDto, CountryDto, Long> {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Override
    public Iterable<CountryDto> getAll() {
        try {
            Iterable<CountryEntity> countries = countryRepository.findAll();
            return StreamSupport.stream(countries.spliterator(), false)
                    .map(countryMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CountryDto read(Long id) {
        try {
            CountryEntity country = countryRepository.findById(id)
                    .orElseThrow(() -> new CountryNotFoundException("No existe un país con el id: " + id));
            return countryMapper.toDto(country);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public CountryEntity getCountry(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("No existe un pais con el id: " + id));
    }

    @Override
    public CountryDto create(CountryDto dto) {
        try {
            CountryEntity country = countryMapper.toEntity(dto);
            return countryMapper.toDto(country);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CountryDto update(CountryDto dto, Long id) {
        try {
            CountryEntity country = countryRepository.findById(id)
                    .orElseThrow(() -> new CountryNotFoundException("No existe un país con el id: " + id));
            country.setName(dto.getName());
            countryRepository.save(country);
            return countryMapper.toDto(country);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CountryDto delete(Long id) {
        try {
            CountryEntity country = countryRepository.findById(id)
                    .orElseThrow(() -> new CountryNotFoundException("No existe un país con el id: " + id));
            countryRepository.delete(country);
            return countryMapper.toDto(country);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CountryDto toggleStatus(Long id) {
        return null;
    }

    @Override
    public CountryDto toggleUserRole(Long id) {
        return null;
    }
}
