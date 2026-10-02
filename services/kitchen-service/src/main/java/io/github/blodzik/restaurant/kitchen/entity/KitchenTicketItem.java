package io.github.blodzik.restaurant.kitchen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kitchen_ticket_item")
@Getter @Setter
public class KitchenTicketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Kitchen ticket is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private KitchenTicket ticket;

    @NotNull(message = "Source order item id is required")
    @Column(name = "source_order_item_id", nullable = false)
    private Long sourceOrderItemId;

    @NotBlank(message = "Name snapshot is required")
    @Column(name = "name_snapshot", nullable = false)
    private String nameSnapshot;

    @NotNull(message = "Quantity is required")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.PENDING;
}
