package com.proyecto.turisteando.auth;

import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.entities.enums.Role;
import com.proyecto.turisteando.exceptions.customExceptions.AuthenticationFailedException;
import com.proyecto.turisteando.exceptions.customExceptions.UserNotFoundException;
import com.proyecto.turisteando.jwt.JwtService;
import com.proyecto.turisteando.mappers.IUserMapper;
import com.proyecto.turisteando.repositories.IUserRepository;
import com.proyecto.turisteando.services.implement.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserMapper userMapper;

    @Autowired
    EmailService emailService;

    @Transactional(rollbackFor = Exception.class)
    public AuthResponse register(UserRequestDto request) {
        try {
            UserEntity user = UserEntity.builder()
                    .name(request.getName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user);

            // Enviar email de confirmación de registro para el usuario nuevo
//            emailService.sendEmail(user.getEmail(), user.getName());
//            emailService.sendHtmlMessage(user.getEmail(), user.getName(), user.getLastName());
            emailService.sendHtmlTemplate(user.getEmail(), user.getName(), user.getLastName());

            String token = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .accessToken(token)
                    .user(userMapper.toDto(user))
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Ya existe un usuario con ese email");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

            if (!user.getIsActive()) {
                throw new LockedException("Cuenta bloqueada, contacta con soporte");
            }

            String token = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .accessToken(token)
                    .user(userMapper.toDto(user))
                    .build();
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Nombre de usuario o contraseña incorrectos");
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (LockedException e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }
}
