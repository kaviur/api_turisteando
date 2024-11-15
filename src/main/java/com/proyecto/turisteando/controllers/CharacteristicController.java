package com.proyecto.turisteando.controllers;



import java.util.List;
import java.util.stream.StreamSupport;

import com.proyecto.turisteando.dtos.IDto;

import com.proyecto.turisteando.dtos.requestDto.CharacteristicRequestDto;
import com.proyecto.turisteando.dtos.requestDto.ImageRequestDto;
import com.proyecto.turisteando.services.ICharacteristicService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/characteristic")
@Validated
public class CharacteristicController {
    private final ICharacteristicService characteristicService;

    //Get all characteristics
    @GetMapping("/all")
    public ResponseEntity<Response> getAllCharacteristics() {
        Iterable<IDto> characteristicIterable = characteristicService.getAll();
        List<IDto> characteristicList = StreamSupport.stream(characteristicIterable.spliterator(), false)
                .toList();

        Response response = new Response(true, HttpStatus.OK, characteristicList);

        return ResponseEntity.ok(response);
    }

    //Get characteristic by id
    @GetMapping("/{id}")
    public ResponseEntity<Response> getCharacteristicById(@PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, characteristicService.read(id));
        return ResponseEntity.ok(response);
    }

    //create  characteristic
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> createCharacteristic(
            @Validated @RequestPart("characteristic") CharacteristicRequestDto characteristicDto,
            @RequestPart("image") MultipartFile image) {

        // Asignar la imagen al DTO manualmente
        characteristicDto.setIcon(image);
        IDto newCharacteristic = characteristicService.create(characteristicDto);

        Response response = new Response(true, HttpStatus.CREATED, newCharacteristic);

        return ResponseEntity.ok(response);
    }


    //update  characteristic
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> updateCharacteristic(
            @PathVariable Long id,
            @Valid @RequestPart("characteristic") CharacteristicRequestDto characteristicDto,
            @RequestPart(value = "image", required = false) MultipartFile icon) {

        if (icon != null && !icon.isEmpty()) {
            characteristicDto.setIcon(icon);
        }

        Response response = new Response(true, HttpStatus.OK, characteristicService.update(characteristicDto, id));

        return ResponseEntity.ok(response);
    }

    //delete  characteristic
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCharacteristic(@PathVariable Long id) {
        characteristicService.delete(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, "Característica eliminada con éxito"));
    }

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleStatus(@PathVariable Long id) {
        IDto characteristicDto = characteristicService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, characteristicDto));
    }
}
