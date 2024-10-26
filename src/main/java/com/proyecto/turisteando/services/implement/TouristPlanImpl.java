package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TouristPlanImpl implements ICrudService<TouristPlanEntity, Long> {

    @Autowired
    private TouristPlanRepository touristPlanRepository;

    @Override
    public Iterable<TouristPlanEntity> getAll() {
        return touristPlanRepository.findAll();
    }

    @Override
    public TouristPlanEntity read(Long id) {

        return touristPlanRepository.findById(id).orElse(null);
    }

    @Override
    public TouristPlanEntity create(TouristPlanEntity dto) {

        return touristPlanRepository.save(dto);
    }

    @Override
    public TouristPlanEntity update(TouristPlanEntity dto, Long id) {
        return touristPlanRepository.findById(id).map(entity -> {
            entity.setTitle(dto.getTitle());
            entity.setDescription(dto.getDescription());
            entity.setPrice(dto.getPrice());
            return touristPlanRepository.save(entity);
        }).orElse(null);
    }

    @Override
    public TouristPlanEntity delete(Long id) {
        TouristPlanEntity entity = touristPlanRepository.findById(id).orElse(null);
        if (entity != null) touristPlanRepository.delete(entity);
        return entity;
    }

    @Override
    public TouristPlanEntity toggleStatus(Long id) {
        TouristPlanEntity entity = touristPlanRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setActive(!entity.isActive());
            return touristPlanRepository.save(entity);
        }
        return null;
    }
}
