package io.github.blodzik.restaurant.kitchen.event;

public record BatchItemDTO(
        Long sourceOrderItemId,
        String nameSnapshot,
        Integer quantity,
        String destinationSnapshot
) {
}
