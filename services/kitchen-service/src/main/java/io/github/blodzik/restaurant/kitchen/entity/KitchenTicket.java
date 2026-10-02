package io.github.blodzik.restaurant.kitchen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kitchen_ticket")
@Getter @Setter
public class KitchenTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Source batch id is required")
    @Column(name = "source_batch_id", nullable = false)
    private Long sourceBatchId;

    @NotBlank(message = "Table label snapshot is required")
    @Column(name = "table_label_snapshot", nullable = false)
    private String tableLabelSnapshot;

    @NotNull(message = "Batch number is required")
    @Column(name = "batch_number", nullable = false)
    private Integer batchNumber;

    @Column(name = "fired_at")
    private LocalDateTime firedAt;

    @NotBlank(message = "Waiter name is required")
    @Column(nullable = false)
    private String waiterName;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenTicketItem> items = new ArrayList<>();
}
