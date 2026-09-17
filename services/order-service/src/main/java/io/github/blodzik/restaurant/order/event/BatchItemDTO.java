package io.github.blodzik.restaurant.order.event;

public record BatchItemDTO(
        Long sourceOrderItemId,
        String nameSnapshot,
        Integer quantity,
        String destinationSnapshot
) {
}
