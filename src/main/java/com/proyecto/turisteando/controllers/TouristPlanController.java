package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.requestDto.UserFavoriteTouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.services.ITouristPlanService;
import com.proyecto.turisteando.utils.Response;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tourist-plans")
public class TouristPlanController {

    @Autowired
    private ITouristPlanService touristPlanService;

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allfavoritesbyuser")
    public ResponseEntity<Response> findAllFavoritesByUser(@AuthenticationPrincipal UserEntity user) {
        List<TouristPlanResponseDto> touristPlans = touristPlanService.findAllFavoritesByUser(user);
        Response response = new Response(true, HttpStatus.OK, touristPlans);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/addFavoriteToUser")
    public ResponseEntity<Response> addUserFavoritePlan(@RequestBody UserFavoriteTouristPlanRequestDto userPlans) {
        touristPlanService.addUsersFavorites(userPlans.getUserId(), userPlans.getPlanId());
        Response response = new Response(true, HttpStatus.OK, "Plan agregado a favoritos");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/deleteFavoriteToUser")
    public ResponseEntity<Response> deleteUserFavoritePlan(@RequestBody UserFavoriteTouristPlanRequestDto userPlans) {
        touristPlanService.deleteUsersFavorites(userPlans.getUserId(), userPlans.getPlanId());
        Response response = new Response(true, HttpStatus.OK, "Plan eliminado de favoritos");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteTouristPlan(@PathVariable Long id) {
        TouristPlanResponseDto deletedTouristPlan = touristPlanService.delete(id);
        Response response = new Response(true, HttpStatus.OK, deletedTouristPlan);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleTouristPlanStatus(@PathVariable Long id) {
        TouristPlanResponseDto toggledTouristPlan = touristPlanService.toggleStatus(id);
        Response response = new Response(true, HttpStatus.OK, toggledTouristPlan);
        return ResponseEntity.ok(response);
    }
}
