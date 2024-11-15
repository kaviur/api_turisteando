package com.proyecto.turisteando.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Entity class representing a category.
 * @author Karen Urbano - <a href="https://github.com/kaviur">kaviur</a>
 * @version 1.0
 * @since 2024-10-25
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "El nombre de la categoría no puede estar vacío")
    private String name;

    @Column(nullable = false) // it has no effect, but it is good to have it for clarity
    private Byte status;

    @Size(max = 255, message = "La descripción de la categoría no puede tener más de 255 caracteres")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity image;

    @PrePersist
    protected void onCreate() {
        this.status = 1;
    }

}

