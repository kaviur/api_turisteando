package com.proyecto.turisteando.config;

import com.proyecto.turisteando.entities.enums.Role;
import com.proyecto.turisteando.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationProvider authenticationProvider;

    @Autowired
    JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
//                            .requestMatchers("/api/categories/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers("/api/cities/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers("/api/images/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers("/api/reservations/**").hasAnyAuthority(Role.ADMIN.name(), Role.BUYER.name())
//                            .requestMatchers("/api/reviews/**").hasAnyAuthority(Role.ADMIN.name(), Role.BUYER.name())
//                            .requestMatchers(HttpMethod.POST,  "/api/tourist-plans/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers(HttpMethod.PUT, "/api/tourist-plans/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers(HttpMethod.DELETE, "/api/tourist-plans/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers(HttpMethod.PATCH, "/api/tourist-plans/**").hasAuthority(Role.ADMIN.name())
//                            .requestMatchers("/api/users/**").hasAuthority(Role.ADMIN.name())
                            .anyRequest().permitAll();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

