package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.CityRequestDto;
import com.proyecto.turisteando.dtos.responseDto.CityResponseDto;
import com.proyecto.turisteando.services.CrudService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CrudService<CityRequestDto, CityResponseDto, Long> cityService;

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody CityRequestDto city) {
        Response response = new Response(true, HttpStatus.CREATED, cityService.create(city));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        List<CityResponseDto> cities = (List<CityResponseDto>) cityService.getAll();
        Response response = new Response(true, HttpStatus.OK, cities);
        if (cities.isEmpty()) {
            response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron ciudades");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, cityService.read(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> update(@RequestBody CityRequestDto city, @PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, cityService.update(city, id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, cityService.delete(id));
        return ResponseEntity.ok(response);
    }
}
