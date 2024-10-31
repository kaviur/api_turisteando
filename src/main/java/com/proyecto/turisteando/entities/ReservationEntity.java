package com.proyecto.turisteando.entities;

        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import org.hibernate.annotations.CreationTimestamp;

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

    //@ManyToOne()
    //@JoinColumn(name = "id_plan", nullable = false)
    //private TouristPlanEntity touristPlan;

    @CreationTimestamp()
    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "status", nullable = false)
    private String status; // Confirmed, Pending, Cancelled

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    //@Column(name = "people_count", nullable = false)
    //private int peopleCount;

    @PrePersist
    protected void onCreate() {
        this.reservationDate = LocalDateTime.now();
    }
}
