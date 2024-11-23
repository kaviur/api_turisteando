package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.FavoriteRequestDto;
import com.proyecto.turisteando.dtos.responseDto.FavoriteResponseDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;
import com.proyecto.turisteando.services.IFavoriteService;
import com.proyecto.turisteando.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/favorites")
@Validated
public class FavoritesController {

    @Autowired
    private IFavoriteService favoriteService;


    @GetMapping
    public ResponseEntity<Response> getAllFavorites() {
        Iterable<FavoriteResponseDto> favoriteIterable = favoriteService.getAll();
        List<FavoriteResponseDto> favoriteList = StreamSupport.stream(favoriteIterable.spliterator(), false)
                .toList();
        Response response = new Response(true, HttpStatus.OK, favoriteList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponseDto> getFavoriteById(@PathVariable Long id) {
        FavoriteResponseDto favorite = favoriteService.read(id);
        return ResponseEntity.ok(favorite);
    }


    @PostMapping("/create")
    public ResponseEntity<Response> createFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        Response response = new Response(true, HttpStatus.OK, favoriteService.create(favoriteRequestDto));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateFavorite(
            @RequestBody FavoriteRequestDto favoriteRequestDto,
            @PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, favoriteService.update(favoriteRequestDto, id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteFavorite(@PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, "Favorito eliminado exitosamente"));
    }


    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Response> toggleStatus(@PathVariable Long id) {
        FavoriteResponseDto updatedFavorite = favoriteService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, updatedFavorite));
    }


}
