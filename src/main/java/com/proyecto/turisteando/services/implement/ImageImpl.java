package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.entities.ImageEntity;
import com.proyecto.turisteando.repositories.ImageRepository;
import com.proyecto.turisteando.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class ImageImpl implements ICrudService<ImageEntity, Long> {


    private final ImageRepository imageRepository;


    @Override
    public Iterable<ImageEntity> getAll() {
        return imageRepository.findAll();
    }

    @Override
    public ImageEntity read(Long id) {
        return imageRepository.findById(id).orElse(null);

    }

    @Override
    public ImageEntity create(ImageEntity dto) {
        return imageRepository.save(dto);
    }

    @Override
    public ImageEntity update(ImageEntity dto, Long id) {

        return imageRepository.findById(id).map(entity -> {
            entity.setImageUrl(dto.getImageUrl());
            return imageRepository.save(entity);
        }).orElse(null);
    }

    @Override
    public ImageEntity delete(Long id) {
        ImageEntity entity = imageRepository.findById(id).orElse(null);
        if (entity != null) {
            imageRepository.delete(entity);
        }
        return entity;
    }

    @Override
    public ImageEntity toggleStatus(Long id) {
        return null;
    }
}
