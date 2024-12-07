package com.proyecto.turisteando.entities;

        import com.fasterxml.jackson.annotation.JsonBackReference;
        import com.fasterxml.jackson.annotation.JsonIgnore;
        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import org.hibernate.annotations.CreationTimestamp;

        import java.time.LocalDate;
        import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reservation")
public class ReservationEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "id_plan", nullable = false)
    private TouristPlanEntity touristPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @CreationTimestamp()
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //antes reservationDate

    @Column(name = "status", nullable = false)
    private boolean status; // true para activo, false para inactivo

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "people_count", nullable = false)
    private int peopleCount;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @PrePersist
    protected void onCreate() { this.status = true; }
}
