package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.entities.ImageEntity;
import com.proyecto.turisteando.services.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ICrudService <ImageEntity, Long> imageService;

    @PostMapping("/create")

    public ImageEntity createImage(@RequestBody ImageEntity image) {
        return imageService.create(image);
    }


    @GetMapping("/id")
    public ImageEntity getImage(@PathVariable Long id) {
        return imageService.read(id);
    }

    @GetMapping("/all")
    public Iterable<ImageEntity> getAllImages() {
        return imageService.getAll();
    }

    @PutMapping("/update/{id}")
    public ImageEntity updateImage(@RequestBody ImageEntity imageEntity, @PathVariable Long id) {
        return imageService.update(imageEntity, id);
    }

    @DeleteMapping("/delete/{id}")
    public ImageEntity deleteTouristPlan(@PathVariable Long id) {
        return imageService.delete(id);
    }

    @PutMapping("/toggle-status/{id}")
    public ImageEntity toggleImageStatus(@PathVariable Long id) {
        return imageService.toggleStatus(id);
    }
}
