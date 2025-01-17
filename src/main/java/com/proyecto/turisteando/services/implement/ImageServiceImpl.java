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
import com.proyecto.turisteando.services.IImageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor

public class ImageServiceImpl implements IImageService {


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
            throw new ServiceException(e.getMessage());
        }


    }



    @Override
    public IDto create(IDto dto) {
        ImageRequestDto imageRequestDto = (ImageRequestDto) dto;
        try {
            TouristPlanEntity touristPlanEntity = touristPlanRepository.findById(imageRequestDto.getTouristPlanId())
                    .orElseThrow(() -> new TouristPlanNotFoundException("No existe un plan Turístico con ese id" + imageRequestDto.getTouristPlanId()));

             ImageEntity imageEntity = imageMapper.toEntity(imageRequestDto);
             imageEntity.setTouristPlan(touristPlanEntity);
             imageRepository.save(imageEntity);

        return imageMapper.toDto(imageEntity);

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
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
            throw new ServiceException(e.getMessage());
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
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public IDto toggleStatus(Long id) {
        return null;
    }
}
