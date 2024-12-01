package com.proyecto.turisteando.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_plan_id", nullable = false)
    @JsonIgnoreProperties("reviews") // Evitar recursión al serializar
    private TouristPlanEntity touristPlan; // Relación con el plan turístico

    private int rating;

    @Size(max = 500, message = "El comentario no puede tener más de 500 caracteres")
    private String comment;

    @Temporal(TemporalType.DATE)
    private Date date;

    private Byte status;

    @PrePersist
    protected void onCreate() {
        this.date = new Date();
        this.status = 1;
    }
}
