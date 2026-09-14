package io.github.blodzik.restaurant.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_batch")
@Getter @Setter
public class OrderBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Table id is required")
    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @NotNull(message = "Batch number is required")
    @Column(name = "batch_number", nullable = false)
    private Integer batchNumber;

    @Column(name = "fired_at")
    private LocalDateTime firedAt;

    @Column(name = "fired_by")
    private String firedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
