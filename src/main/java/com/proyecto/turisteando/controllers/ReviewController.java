package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;
import com.proyecto.turisteando.services.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody @Valid ReviewRequestDto reviewDto) {
        return ResponseEntity.ok(reviewService.create(reviewDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewRequestDto reviewDto) {
        return ResponseEntity.ok(reviewService.update(reviewDto, id));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.delete(id));
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<ReviewResponseDto> disableReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.toggleStatus(id));
    }

    @GetMapping("/plan/{idPlan}")
    public ResponseEntity<Iterable<ReviewResponseDto>> getAllReviewsByPlan(Long idPlan) {
        Iterable<ReviewResponseDto> reviews = reviewService.getAllByPlan(idPlan);
        List<ReviewResponseDto> reviewList = StreamSupport.stream(reviews.spliterator(), false)
                .toList();
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/rating/{idPlan}/{rating}")
    public ResponseEntity<Iterable<ReviewResponseDto>> getAllReviewsByRating(Long idPlan, int rating) {
        Iterable<ReviewResponseDto> reviews = reviewService.getAllByRating(idPlan, rating);
        List<ReviewResponseDto> reviewList = StreamSupport.stream(reviews.spliterator(), false)
                .toList();
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(Long id) {
        return ResponseEntity.ok(reviewService.read(id));
    }
}
