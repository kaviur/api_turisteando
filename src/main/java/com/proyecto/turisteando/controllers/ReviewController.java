package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.services.IReviewService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllReviews() {
        Iterable<ReviewResponseDto> reviewResponseDto = reviewService.getAll();
        if (!reviewResponseDto.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron reseñas");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, reviewResponseDto));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createReview(@RequestBody @Valid ReviewRequestDto reviewDto) {
        Response response = new Response(
                true,
                HttpStatus.OK,
                reviewService.create(reviewDto)
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewRequestDto reviewDto) {
        Response response = new Response(
                true,
                HttpStatus.OK,
                reviewService.update(reviewDto, id)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok(new Response(
                true,
                HttpStatus.OK,
                "Review deleted successfully"
        ));
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<Response> disableReview(@PathVariable Long id) {
        ReviewResponseDto updatedReviewResponseDto = reviewService.toggleStatus(id);
        return ResponseEntity.ok(new Response(
                true,
                HttpStatus.OK,
                updatedReviewResponseDto
        ));
    }

    @GetMapping("/plan/{idPlan}")
    public ResponseEntity<Response> getAllReviewsByPlan(@PathVariable Long idPlan) {
        Iterable<ReviewResponseDto> reviews = reviewService.getAllByPlan(idPlan);
        List<ReviewResponseDto> reviewList = StreamSupport.stream(reviews.spliterator(), false)
                .toList();
        if (reviewList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Aún no hay reseñas para este producto"
            ));
        }
        return ResponseEntity.ok(new Response(
                true,
                HttpStatus.OK,
                reviewList
        ));
    }

    @GetMapping("/plan/{idPlan}/reviews")
    public ResponseEntity<Response> getAllReviewsByPlanPage(
            @PageableDefault(page = 0, size = 2, sort = "id") Pageable pageable,
            @PathVariable Long idPlan) {

        Page<ReviewResponseDto> reviews = reviewService.getAllByPlanP(idPlan, pageable);
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Aún no hay reseñas para este producto"
            ));
        }
        return ResponseEntity.ok(new Response(
                true,
                HttpStatus.OK,
                reviews
        ));
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<Response> getAllReviewsByUser(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @PathVariable Long idUser) {
        Page<ReviewResponseDto> reviews = reviewService.getAllByUser(idUser, pageable);
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(
                    false,
                    HttpStatus.NOT_FOUND,
                    "No tienes reseñas aún"
            ));
        }
        return ResponseEntity.ok(new Response(
                true,
                HttpStatus.OK,
                reviews
        ));
    }

    @GetMapping("/rating/{idPlan}/{rating}")
    public ResponseEntity<Response> getAllReviewsByRating(@PathVariable Long idPlan, @PathVariable int rating) {
        Iterable<ReviewResponseDto> reviews = reviewService.getAllByRating(idPlan, rating);
        List<ReviewResponseDto> reviewList = StreamSupport.stream(reviews.spliterator(), false)
                .toList();
        if (reviewList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Aún no hay reseñas para este producto con ese rating"
            ));
        }
        return ResponseEntity.ok(new Response(
                true,
                HttpStatus.OK,
                reviewList
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getReviewById(@PathVariable Long id) {
        Response response = new Response(
                true,
                HttpStatus.OK,
                reviewService.read(id)
        );
        return ResponseEntity.ok(response);
    }
}
