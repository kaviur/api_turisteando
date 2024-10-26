package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.services.ICrudService;
import com.proyecto.turisteando.services.implement.TouristPlanImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tourist-plan")
public class TouristPlanController {

    @Autowired
    private ICrudService<TouristPlanEntity, Long> touristPlanService;

    @PostMapping("/create")
    public TouristPlanEntity createTouristPlan(@RequestBody TouristPlanEntity touristPlan) {
        return touristPlanService.create(touristPlan);
    }

    @GetMapping("/{id}")
    public TouristPlanEntity getTouristPlan(@PathVariable Long id) {
        return touristPlanService.read(id);
    }

    @GetMapping("/all")
    public Iterable<TouristPlanEntity> getAllTouristPlans() {
        return touristPlanService.getAll();
    }

    @PutMapping("/update/{id}")
    public TouristPlanEntity updateTouristPlan(@RequestBody TouristPlanEntity touristPlan, @PathVariable Long id) {
        return touristPlanService.update(touristPlan, id);
    }

    @DeleteMapping("/delete/{id}")
    public TouristPlanEntity deleteTouristPlan(@PathVariable Long id) {
        return touristPlanService.delete(id);
    }

    @PutMapping("/toggle-status/{id}")
    public TouristPlanEntity toggleTouristPlanStatus(@PathVariable Long id) {
        return touristPlanService.toggleStatus(id);
    }
}
