package com.proyecto.turisteando.controllers;


import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.CategoryRequestDto;
import com.proyecto.turisteando.services.ICategoryService;
import com.proyecto.turisteando.services.ICrudService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/categories")
@Validated
public class CategoryController {

    private final ICategoryService categoryService;

    //get all categories
    @GetMapping("/all")
    public ResponseEntity<Response> getAllCategories() {
        Iterable<IDto> categoryIterable = categoryService.getAll();
        List<IDto> categoryList = StreamSupport.stream(categoryIterable.spliterator(), false)
                .toList();
        Response response = new Response(true, HttpStatus.OK, categoryList);
        return ResponseEntity.ok(response);
    }

    //get category by id
    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategoryById(@PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, categoryService.read(id));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Response> createCategory(@Valid @RequestBody CategoryRequestDto categoryDto) {
        Response response = new Response(true, HttpStatus.OK, categoryService.create(categoryDto));
        return ResponseEntity.ok(response);
    }

    //create category
    //update category
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto categoryDto) {
        Response response = new Response(true, HttpStatus.OK, categoryService.update(categoryDto, id));
        return ResponseEntity.ok(response);
    }

    //delete category
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, "Categoría eliminada exitosamente"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleStatus(@PathVariable Long id) {
        IDto categoryDto = categoryService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, categoryDto));
    }

}

