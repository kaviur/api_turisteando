package com.proyecto.turisteando.controllers;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.services.IUserService;
import com.proyecto.turisteando.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<Response> getUser() {
        Iterable<UserResponseDto> userResponseDtos = userService.getAll();
        if (!userResponseDtos.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron usuarios");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDtos));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchUser(UserRequestDto userRequestDto) {
        Iterable<UserResponseDto> userResponseDtos = userService.getAllByFilters(userRequestDto);
        if (!userResponseDtos.iterator().hasNext()) {
            Response response = new Response(false, HttpStatus.NO_CONTENT, "No se encontraron usuarios");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userService.read(id)));
    }

    @GetMapping("/current-user")
    public ResponseEntity<Response> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userService.getCurrentUser(authentication)));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.update(userRequestDto, id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDto));
    }

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<Response> toggleUserStatus(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.toggleStatus(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDto));
    }

    @PatchMapping("/toggle-role/{id}")
    public ResponseEntity<Response> toggleUserRole(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.toggleUserRole(id);
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userResponseDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(new Response(true, HttpStatus.OK, userService.delete(id)));
    }

}
