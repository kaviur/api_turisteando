package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReservationResponseDto;
import com.proyecto.turisteando.services.IReservationService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
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
@RequestMapping("api/reservations")
@Validated
public class ReservationController {

    @Autowired
    private IReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllReservations() {
        Iterable<ReservationResponseDto> reservationIterable = reservationService.getAll();
        List<ReservationResponseDto> reservationList = StreamSupport.stream(reservationIterable.spliterator(), false)
                .toList();

        Response response = new Response(true, HttpStatus.OK, reservationList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getReservationById(@PathVariable Long id) {
        Response response = new Response(true, HttpStatus.OK, reservationService.read(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createReservation(@RequestBody @Valid ReservationRequestDto reservationRequestDto) {
        Response response = new Response(true, HttpStatus.OK, reservationService.create(reservationRequestDto));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateReservation(@PathVariable Long id, @RequestBody @Valid ReservationRequestDto reservationRequestDto) {
        Response response = new Response(true, HttpStatus.OK, reservationService.update(reservationRequestDto, id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, "Reserva eliminada exitosamente"));
    }

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleStatus(@PathVariable Long id) {
        ReservationResponseDto reservationDto = reservationService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, reservationDto));
    }

}