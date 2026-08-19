package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.MenuItem;
import io.github.blodzik.restaurant.menu.repository.MenuItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemRepository menuItemRepository;

    public MenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    List<MenuItem> all() {
        return menuItemRepository.findAll();
    }

    @PostMapping
    MenuItem create(@Valid @RequestBody MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @GetMapping("/{id}")
    MenuItem get(@PathVariable Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    MenuItem update(@PathVariable Long id, @Valid @RequestBody MenuItem menuItem) {
        menuItem.setId(id);
        return menuItemRepository.save(menuItem);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        menuItemRepository.deleteById(id);
    }
}
