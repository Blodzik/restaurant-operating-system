package io.github.blodzik.restaurant.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item_modifier")
@Getter @Setter
public class OrderItemModifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order item id is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @NotBlank(message = "Modifier name is required")
    @Column(name = "modifier_name", nullable = false)
    private String modifierName;

    @NotNull(message = "Price delta snapshot is required")
    @Column(name = "price_delta_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceDeltaSnapshot;
}
