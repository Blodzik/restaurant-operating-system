package io.github.blodzik.restaurant.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
public class Modifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Price delta is required")
    @PositiveOrZero(message = "Price delta cannot be negative")
    @Column(name = "price_delta" ,nullable = false, precision = 10, scale = 2)
    private BigDecimal priceDelta;

    public Modifier() {
    }
}
