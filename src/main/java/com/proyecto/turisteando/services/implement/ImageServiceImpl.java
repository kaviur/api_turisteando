package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.ImageRequestDto;
import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.entities.ImageEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.exceptions.customExceptions.ImageNotFoundException;
import com.proyecto.turisteando.exceptions.customExceptions.TouristPlanNotFoundException;
import com.proyecto.turisteando.mappers.ImageMapper;
import com.proyecto.turisteando.repositories.ImageRepository;

import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor

public class ImageServiceImpl implements ICrudService<IDto, Long> {


    private final ImageRepository imageRepository;
    private final TouristPlanRepository touristPlanRepository;
    private final ImageMapper imageMapper;

    @Override
    public Iterable<IDto> getAll() {
        Iterable<ImageEntity> allImages = imageRepository.findAll();
        return StreamSupport.stream(allImages.spliterator(), false)
                .map(imageMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public IDto read(Long id) {
        try{
            ImageEntity imageEntity= imageRepository.findById(id)
                .orElseThrow(()-> new ImageNotFoundException("No existe una imagen con ese id" + id));
            return imageMapper.toDto(imageEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }



    @Override
    public IDto create(IDto dto) {
        try {
            TouristPlanEntity touristPlanEntity = touristPlanRepository.findById(dto.getTouristPlanId())
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan Turístico con ese id" + dto.getTouristPlanId()));
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageUrl(dto.getImageUrl());
        imageEntity.setIdTouristPlan(touristPlanEntity);

        imageRepository.save(imageEntity);

        return imageMapper.toDto(imageEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public IDto update(IDto dto, Long id) {

        try {
            ImageEntity imageEntity = imageRepository.findById(id)
                    .orElseThrow(() -> new ImageNotFoundException("No existe una imagen con el id: " + id));
            imageMapper.partialUpdate((ImageRequestDto) dto, imageEntity);
            return imageMapper.toDto(imageRepository.save(imageEntity));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public IDto delete(Long id){
        try {
            ImageEntity imageEntity = imageRepository.findById(id)
                    .orElseThrow(() -> new ImageNotFoundException("No existe una imagen con el id: " + id));

            imageRepository.save(imageEntity);
            return imageMapper.toDto(imageEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public IDto toggleStatus(Long id) {
        return null;
    }
}
