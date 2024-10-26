package com.proyecto.turisteando.controllers;


import com.proyecto.turisteando.dtos.requestDto.ReservationDto;
import com.proyecto.turisteando.services.ICrudService;
import com.proyecto.turisteando.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final ICrudService<ReservationDto, Long> reservationService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllReservations() {
        Iterable<ReservationDto> reservationIterable = reservationService.getAll();
        List<ReservationDto> reservationList = StreamSupport.stream(reservationIterable.spliterator(), false)
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
    public ResponseEntity<Response> createReservation(@RequestBody @Valid ReservationDto reservationDto) {
        Response response = new Response(true, HttpStatus.OK, reservationService.create(reservationDto));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateReservation(@PathVariable Long id, @RequestBody @Valid ReservationDto reservationDto) {
        Response response = new Response(true, HttpStatus.OK, reservationService.update(reservationDto, id));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Response> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, "Reserva eliminada exitosamente"));
    }

    // Cambiar estado de la reserva
    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleStatus(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, reservationDto));
    }

}
