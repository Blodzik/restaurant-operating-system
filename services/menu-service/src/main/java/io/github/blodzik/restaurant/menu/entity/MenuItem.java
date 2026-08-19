package io.github.blodzik.restaurant.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@Table(name = "menu_item")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Category ID is required")
    @Column(name = "category_id",nullable = false)
    private Long categoryId;

    @NotBlank(message = "Menu item is required")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull(message = "Base price is required")
    @PositiveOrZero(message = "Base price cannot be negative")
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private boolean active = true;

    @NotNull(message = "Destination is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Destination destination;

    @Column(name = "track_stock", nullable = false)
    private boolean trackStock = false;

    @PositiveOrZero(message = "Stock count cannot be negative")
    @Column(name = "stock_count")
    private Integer stockCount;

    public MenuItem() {
    }
}
