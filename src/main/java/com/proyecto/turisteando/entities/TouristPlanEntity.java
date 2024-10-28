package com.proyecto.turisteando.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tourist_plan")
public class TouristPlanEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String description;

    private Double price;

    // Campos  para el plan turístico con relaciones de entidades pendientes
    private String seller;
    private String city;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany(mappedBy = "idTouristPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("idTouristPlan")
    private List<ImageEntity> image;

    private LocalDate availabilityStartDate;
    private LocalDate availabilityEndDate;
    private Integer capacity;
    private String duration; // Duración del plan turístico (por ejemplo, "3 días" o "1 noche")
    private boolean foodIncluded;
    private boolean wifiIncluded;
    private boolean petsFriendly;
    private boolean disabilityAccess;
    private boolean isActive;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.isActive = true;
    }

}
