package io.github.blodzik.restaurant.floor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@jakarta.persistence.Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Label is required")
    @Column(nullable = false)
    private String label;

    @NotNull(message = "Capacity is required")
    @Column(nullable = false)
    private Integer capacity;

    @NotNull(message = "RestaurantTable State is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "table_state", nullable = false)
    private TableState tableState;

    @Version
    private Long version;

    @Column(name = "occupied_since")
    private LocalDateTime occupiedSince;
}
