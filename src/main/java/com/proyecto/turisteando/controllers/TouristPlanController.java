package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.requestDto.UserFavoriteTouristPlan;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.services.ITouristPlanService;
import com.proyecto.turisteando.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/tourist-plans")
public class TouristPlanController {

    @Autowired
    private ITouristPlanService touristPlanService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> createTouristPlan(
            @Validated @RequestPart("touristPlan") TouristPlanRequestDto touristPlan,
            @RequestPart("images") List<MultipartFile> images) {

        touristPlan.setMultipartImages(images); // Añadir las imágenes al DTO manualmente
        TouristPlanResponseDto newTouristPlan = touristPlanService.create(touristPlan);
        Response response = new Response(true, HttpStatus.CREATED, newTouristPlan);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTouristPlan(@PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, touristPlanService.read(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTouristPlans() {
        Iterable<TouristPlanResponseDto> allTouristPlans = touristPlanService.getAll();

        if (!allTouristPlans.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron planes turísticos");
            return ResponseEntity.ok(response);
        }
        Response response = new Response(true, HttpStatus.OK, allTouristPlans);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchTouristPlans(@ModelAttribute TouristPlanRequestDto iDto) {
        Iterable<TouristPlanResponseDto> touristPlans = touristPlanService.getAllByFilters(iDto);

        if (!touristPlans.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron planes turísticos");
            return ResponseEntity.ok(response);
        }
        Response response = new Response(true, HttpStatus.OK, touristPlans);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allfavoritesbyuserid/{userId}")
    public ResponseEntity<Response> findAllFavoritesByUserId (@PathVariable Long userId) {
        Response response = new Response(true, HttpStatus.OK, touristPlanService.findAllFavoritesByUserId(userId));
        return ResponseEntity.ok(response);
    }


    @PutMapping("/addFavoriteToUser")
    public ResponseEntity<Response> addUserFavoritePlan (@RequestBody UserFavoriteTouristPlan userPlans) {
        touristPlanService.addUsersFavorites(userPlans.getUserId(), userPlans.getPlanId());
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/deleteFavoriteToUser")
    public ResponseEntity<Response> deleteUserFavoritePlan (@RequestBody UserFavoriteTouristPlan userPlans) {
        touristPlanService.deleteUsersFavorites(userPlans.getUserId(), userPlans.getPlanId());
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> updateTouristPlan(
            @RequestPart("touristPlan") TouristPlanRequestDto touristPlan,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @PathVariable Long id) {

        // Añadir las imágenes al DTO solo si están presentes
        if (images != null && !images.isEmpty()) {
            touristPlan.setMultipartImages(images);
        }

        TouristPlanResponseDto updatedTouristPlan = touristPlanService.update(touristPlan, id);
        Response response = new Response(true, HttpStatus.OK, updatedTouristPlan);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteTouristPlan(@PathVariable Long id) {
        TouristPlanResponseDto deletedTouristPlan = touristPlanService.delete(id);
        Response response = new Response(true, HttpStatus.OK, deletedTouristPlan);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleTouristPlanStatus(@PathVariable Long id) {
        TouristPlanResponseDto toggledTouristPlan = touristPlanService.toggleStatus(id);
        Response response = new Response(true, HttpStatus.OK, toggledTouristPlan);
        return ResponseEntity.ok(response);
    }
}
