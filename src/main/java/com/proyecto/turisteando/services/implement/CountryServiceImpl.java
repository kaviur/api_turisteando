package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.entities.CountryEntity;
import com.proyecto.turisteando.exceptions.customExceptions.CountryNotFoundException;
import com.proyecto.turisteando.repositories.CountryRepository;
import com.proyecto.turisteando.services.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements ICrudService<CountryEntity, Long> {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Iterable<CountryEntity> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public CountryEntity read(Long id) {
        try {
            return countryRepository.findById(id)
                    .orElseThrow(() -> new CountryNotFoundException("No existe una ciudad con el id: " + id));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CountryEntity create(CountryEntity dto) {
        try {
            return countryRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CountryEntity update(CountryEntity dto, Long id) {
        try {
            CountryEntity country = countryRepository.findById(id)
                    .orElseThrow(() -> new CountryNotFoundException("No existe un pais con el id: " + id));
            country.setName(dto.getName());
            return countryRepository.save(country);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CountryEntity delete(Long id) {
        try {
            CountryEntity country = countryRepository.findById(id)
                    .orElseThrow(() -> new CountryNotFoundException("No existe un pais con el id: " + id));
            countryRepository.delete(country);
            return country;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CountryEntity toggleStatus(Long id) {
        return null;
    }
}
