package io.github.blodzik.restaurant.kitchen.event;

import java.time.LocalDateTime;
import java.util.List;

public record BatchFireEvent(
        Long tableId,
        Long batchId,
        String tableSnapshot,
        Integer batchNumber,
        LocalDateTime firedAt,
        String waiterName,
        List<BatchItemDTO> items
) {
}
