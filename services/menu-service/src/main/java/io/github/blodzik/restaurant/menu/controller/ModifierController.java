package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Modifier;
import io.github.blodzik.restaurant.menu.service.ModifierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/modifiers")
@RequiredArgsConstructor
public class ModifierController {
    private final ModifierService modifierService;

    @GetMapping
    List<Modifier> all() {
        return modifierService.findAll();
    }

    @GetMapping("/{id}")
    Modifier get(@PathVariable Long id) {
        return modifierService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    Modifier create(@Valid @RequestBody Modifier m) {
        return modifierService.create(m);
    }

    @PutMapping("/{id}")
    Modifier put(@PathVariable Long id, @Valid @RequestBody Modifier m) {
        return modifierService.update(id, m)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        if(!modifierService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
