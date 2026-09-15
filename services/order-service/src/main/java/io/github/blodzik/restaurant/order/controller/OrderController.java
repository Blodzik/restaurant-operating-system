package io.github.blodzik.restaurant.order.controller;

import io.github.blodzik.restaurant.order.dto.request.AddItemRequest;
import io.github.blodzik.restaurant.order.entity.OrderBatch;
import io.github.blodzik.restaurant.order.entity.OrderItem;
import io.github.blodzik.restaurant.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management for tables and guests")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{tableId}/guests/{guestId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add an item to a guest's current batch")
    public OrderItem addItem(
            @PathVariable Long tableId,
            @PathVariable Long guestId,
            @Valid @RequestBody AddItemRequest request
            ) {
        return orderService.addItemToTable(
                tableId,
                guestId,
                request.menuItemId(),
                request.name(),
                request.price(),
                request.destination(),
                request.quantity()
        );
    }

    @PostMapping("/{tableId}/batches/fire")
    @Operation(summary = "Fire the current open batch to the kitchen/bar")
    public OrderBatch fireBatch(
            @PathVariable Long tableId,
            @RequestParam String waiterName
    ) {
        return orderService.fireBatch(tableId, waiterName);
    }


    @GetMapping("/{tableId}/tab")
    @Operation(summary = "Get all ordered items for a table")
    public List<OrderItem> getTableTab(@PathVariable Long tableId) {
        return orderService.getTableTab(tableId);
    }
}
