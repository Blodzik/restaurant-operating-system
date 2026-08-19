package io.github.blodzik.restaurant.menu.controller;

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
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping
    List<MenuItem> all() {
        return menuItemService.findAll();
    }

    @GetMapping("/{id}")
    MenuItem get(@PathVariable Long id) {
        return menuItemService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    MenuItem create(@Valid @RequestBody MenuItem menuItem) {
        return menuItemService.create(menuItem);
    }

    @PutMapping("/{id}")
    MenuItem update(@PathVariable Long id, @Valid @RequestBody MenuItem menuItem) {
        return menuItemService.update(id, menuItem)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        if(!menuItemService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/decrement-stock")
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
