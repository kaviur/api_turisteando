package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.exceptions.customExceptions.CityNotFoundException;
import com.proyecto.turisteando.repositories.CityRepository;
import com.proyecto.turisteando.services.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements ICrudService<CityEntity, Long> {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Iterable<CityEntity> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public CityEntity read(Long id) {
        try {
            return cityRepository.findById(id)
                    .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CityEntity create(CityEntity dto) {
        try {
            return cityRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CityEntity update(CityEntity dto, Long id) {
        try {
            CityEntity city = cityRepository.findById(id)
                    .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
            city.setName(dto.getName());
            return cityRepository.save(city);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CityEntity delete(Long id) {
        try {
            CityEntity city = cityRepository.findById(id)
                    .orElseThrow(() -> new CityNotFoundException("No existe la ciudad con id: " + id));
            cityRepository.delete(city);
            return city;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CityEntity toggleStatus(Long id) {
        return null;
    }
}
