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
    private String imageUrl;

    // Campo de conexión con  para el plan turístico con relaciones pendientes
    @ManyToOne()
    @JoinColumn(name = "tourist_plan_id", nullable = false)
    private TouristPlanEntity idTouristPlan; //Conexión con la tabla Tourist_Plan

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updatedAt;
}
