package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.ImageRequestDto;

import com.proyecto.turisteando.services.IImageService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

   private final IImageService imageService;

    @PostMapping("/create")

    public ResponseEntity<Response> createImage(@Valid @RequestBody ImageRequestDto imageRequestDto) {
        Response response = new Response(true, HttpStatus.CREATED,  imageService.create(imageRequestDto));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateImage(
            @RequestBody ImageRequestDto imageRequestDto,
            @PathVariable Long id) {

        Response response = new Response(true,HttpStatus.OK, imageService.update(imageRequestDto, id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteTouristPlan(
            @PathVariable Long id) {

        Response response = new Response(true, HttpStatus.OK, imageService.delete(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
