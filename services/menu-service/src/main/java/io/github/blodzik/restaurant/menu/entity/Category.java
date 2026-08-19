package io.github.blodzik.restaurant.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Column(nullable = false)
    private String name;

    @PositiveOrZero(message = "Display order cannot be negative")
    private Integer displayOrder;

    @Column(nullable = false)
    private boolean active = true;

    public Category() {
    }
}
