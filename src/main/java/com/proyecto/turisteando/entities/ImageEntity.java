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
@Table(name = "image")

public class ImageEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String image_url;
    // Campo de conexión con  para el plan turístico con relaciones pendientes
    private String id_tourist_plan; //Conexión con la tabla Tourist_Plan
    private boolean isActive;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updated_at;
}
