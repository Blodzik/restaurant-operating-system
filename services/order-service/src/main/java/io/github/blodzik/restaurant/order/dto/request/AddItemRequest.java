package io.github.blodzik.restaurant.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AddItemRequest(
        @NotNull(message = "Menu item ID is required")
        Long menuItemId,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price,

        @NotBlank(message = "Destination is required")
        String destination,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be at least 1")
        Integer quantity
) {
}
