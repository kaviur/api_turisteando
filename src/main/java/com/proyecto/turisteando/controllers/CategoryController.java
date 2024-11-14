package com.proyecto.turisteando.controllers;


import com.proyecto.turisteando.dtos.IDto;
import com.proyecto.turisteando.dtos.requestDto.CategoryRequestDto;
import com.proyecto.turisteando.dtos.responseDto.CategoryResponseDto;
import com.proyecto.turisteando.services.ICategoryService;
import com.proyecto.turisteando.services.ICrudService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> createCategory(
            @Validated @RequestPart("category") CategoryRequestDto categoryDto,
            @RequestPart("image") MultipartFile image) {

        // Asignar la imagen al DTO manualmente
        categoryDto.setImage(image);
        IDto newCategory = categoryService.create(categoryDto);

        Response response = new Response(true, HttpStatus.CREATED, newCategory);

        return ResponseEntity.ok(response);
    }

    //update category
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> updateCategory(
            @PathVariable Long id,
            @Valid @RequestPart("category") CategoryRequestDto categoryDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        // Si se proporciona una nueva imagen, la añadimos al DTO
        if (image != null && !image.isEmpty()) {
            categoryDto.setImage(image);
        }

        Response response = new Response(true, HttpStatus.OK, categoryService.update(categoryDto, id));
        return ResponseEntity.ok(response);
    }

    //delete category
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, "Categoría eliminada exitosamente"));
    }

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleStatus(@PathVariable Long id) {
        IDto categoryDto = categoryService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, categoryDto));
    }

}

