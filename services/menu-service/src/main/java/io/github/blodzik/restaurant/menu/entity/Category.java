package io.github.blodzik.restaurant.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer displayOrder;

    @Column(nullable = false)
    private boolean active = true;

    public Category() {
    }
}
