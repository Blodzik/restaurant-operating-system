package io.github.blodzik.restaurant.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_item")
@Getter @Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Batch id is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private OrderBatch batch;

    @NotNull(message = "Guest id is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @NotNull(message = "Menu item id is required")
    @Column(name = "menu_item_id", nullable = false)
    private Long menuItem;

    @NotBlank(message = "Name snapshot is required")
    @Column(name = "name_snapshot", nullable = false)
    private String nameSnapshot;

    @NotNull(message = "Price snapshot is required")
    @PositiveOrZero(message = "Price snapshot cannot be negative")
    @Column(name = "price_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSnapshot;

    @NotBlank(message = "Destination snapshot is required")
    @Column(name = "destination_snapshot", nullable = false)
    private String destinationSnapshot;

    @NotNull(message = "Quantity is required")
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemStatus status;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemModifier> modifiers = new ArrayList<>();
}
