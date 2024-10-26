package com.proyecto.turisteando.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reservation")
public class ReservationEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_reservation")
    private Long id;

    // Relación con la entidad User
    //@ManyToOne
    //@JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
   //private User user;

    // Relación con la entidad TouristPlan
    //@ManyToOne
    //@JoinColumn(name = "id_plan", referencedColumnName = "id_plan", nullable = false)
    //private TouristPlan plan;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "status", nullable = false)
    private String status; // Confirmed, Pending, Cancelled

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
}
