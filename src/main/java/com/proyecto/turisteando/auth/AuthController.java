package com.proyecto.turisteando.auth;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.dtos.responseDto.UserResponseDto;
import com.proyecto.turisteando.jwt.JwtService;
import com.proyecto.turisteando.services.IUserService;
import com.proyecto.turisteando.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Response> register(@Validated @RequestBody UserRequestDto request) {
        AuthResponse authResponse = authService.register(request);
        Response response = new Response(true, HttpStatus.CREATED, authResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request) {
        Response response = new Response(true, HttpStatus.OK, authService.login(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/googleUser")
    public ResponseEntity<Response> processOAuthPostLogin(@AuthenticationPrincipal OAuth2User principal) {
        Response response = new Response(true, HttpStatus.OK, authService.loginWithGoogle(principal));
        return ResponseEntity.ok(response);
    }

}
