package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.TouristPlanRequestDto;
import com.proyecto.turisteando.dtos.responseDto.TouristPlanResponseDto;
import com.proyecto.turisteando.services.ITouristPlanService;
import com.proyecto.turisteando.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tourist-plans")
public class TouristPlanController {

    @Autowired
    private ITouristPlanService touristPlanService;

    @PostMapping("/create")
    public ResponseEntity<Response> createTouristPlan(@Validated @RequestBody TouristPlanRequestDto touristPlan) {
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateTouristPlan(@RequestBody TouristPlanRequestDto touristPlan, @PathVariable Long id) {
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
