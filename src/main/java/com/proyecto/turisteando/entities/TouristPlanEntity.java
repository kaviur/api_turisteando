package com.proyecto.turisteando.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tourist_plans")
public class TouristPlanEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String price;

    // Campos  para el plan turístico con relaciones de entidades pendientes
    private String category;
    private String seller;
    private String city;

    private LocalDate start_date_available;
    private LocalDate end_date_available;
    private int capacity;
    private String duration; // Duración del plan turístico (por ejemplo, "3 días" o "1 noche")
    private boolean food_included;
    private boolean wifi_included;
    private boolean pets_friendly;
    private boolean disability_access;
    private boolean isActive;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updated_at;

}
