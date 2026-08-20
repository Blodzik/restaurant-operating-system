package io.github.blodzik.restaurant.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.github.blodzik.restaurant.menu.entity.MenuItem;
import io.github.blodzik.restaurant.menu.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Menu item management endpoints")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping
    @Operation(summary = "List menu items")
    @ApiResponse(responseCode = "200", description = "Menu items returned")
    List<MenuItem> all() {
        return menuItemService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item found"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    MenuItem get(@PathVariable Long id) {
        return menuItemService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Create menu item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    MenuItem create(@Valid @RequestBody MenuItem menuItem) {
        return menuItemService.create(menuItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update menu item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    MenuItem update(@PathVariable Long id, @Valid @RequestBody MenuItem menuItem) {
        return menuItemService.update(id, menuItem)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item deleted"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    void delete(@PathVariable Long id) {
        if(!menuItemService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/decrement-stock")
    @Operation(summary = "Decrement menu item stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock decremented"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient stock")
    })
    public ResponseEntity<Void> decrementStock(@PathVariable Long id) {
        try {
            boolean success = menuItemService.decrementStock(id);

            if(success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
