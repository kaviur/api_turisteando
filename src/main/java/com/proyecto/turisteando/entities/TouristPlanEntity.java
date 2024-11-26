package com.proyecto.turisteando.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tourist_plan")
public class TouristPlanEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true, nullable = false)
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @Column(length = 500, nullable = false)
    private String description;

    private Double price;

    // Campos  para el plan turístico con relaciones de entidades pendientes
    private String seller;

    @ManyToOne()
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

  
    @OneToMany(mappedBy = "touristPlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("touristPlan")
    @Size(min = 1, max = 5, message = "Debe haber entre 1 y 5 imágenes")
    @JsonManagedReference  // Previene la recursión en el lado "principal"
    private List<ImageEntity> images;

    private LocalDate availabilityStartDate;
    private LocalDate availabilityEndDate;
    private Integer capacity;
    private String duration; // Duración del plan turistico (por ejemplo, "3 días" o "1 noche")

    @ManyToMany
    @JoinTable(
            name = "tourist_plan_characteristic",
            joinColumns = @JoinColumn(name = "tourist_plan_id"),
            inverseJoinColumns =  @JoinColumn(name = "characteristic_id"))
    @JsonManagedReference
    private List<CharacteristicEntity> characteristic;

    @OneToMany(mappedBy = "touristPlan", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("touristPlan") // Evitar recursión al serializar
    @JsonManagedReference // Manejar relaciones bidireccionales
    private List<ReviewEntity> reviews;

    @Column(nullable = false)
    private Integer totalReviews; // Cantidad total de reseñas para obtener el rating

    @Column(nullable = false)
    private Integer totalStars; // Suma total de las estrellas para obtener el rating

    // Relación muchos a muchos con los usuarios que tienen este plan como favorito
    @ManyToMany(mappedBy = "favoritesTouristPlans" )
    @JsonIgnore
    private Set<UserEntity> usersFavorites;

    private boolean isActive;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        if (this.totalReviews == null) {
            this.totalReviews = 0;
        }
        if (this.totalStars == null) {
            this.totalStars = 0;
        }
        this.isActive = true;
    }

}
