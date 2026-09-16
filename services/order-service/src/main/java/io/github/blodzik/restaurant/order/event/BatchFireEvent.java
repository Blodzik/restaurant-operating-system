package io.github.blodzik.restaurant.order.event;

public record BatchFireEvent(
        Long tableId,
        Long batchId,
        String waiterName
) {
}
