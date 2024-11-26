package com.proyecto.turisteando.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "image")
public class ImageEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String imageUrl;

    @ManyToOne()
    @JoinColumn(name = "tourist_plan_id", nullable = true)
    @JsonBackReference  // Evita la recursión en el lado "secundario"
    @JsonIgnore
    private TouristPlanEntity touristPlan;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updatedAt;

}
