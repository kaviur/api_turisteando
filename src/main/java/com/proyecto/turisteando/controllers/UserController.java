package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.services.IUserService;
import com.proyecto.turisteando.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Response> getUser() {
        Iterable<UserResponseDto> userResponseDtos = userService.getAll();
        if (!userResponseDtos.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron usuarios");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDtos));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Response> searchUser(UserRequestDto userRequestDto) {
        Iterable<UserResponseDto> userResponseDtos = userService.getAllByFilters(userRequestDto);
        if (!userResponseDtos.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron usuarios");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDtos));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userService.read(id)));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.update(userRequestDto, id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleUserStatus(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userService.delete(id)));
    }
}
