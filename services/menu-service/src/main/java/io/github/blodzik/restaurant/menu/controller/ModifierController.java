package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Modifier;
import io.github.blodzik.restaurant.menu.repository.ModifierRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/modifiers")
public class ModifierController {
    private final ModifierRepository modifierRepository;

    public ModifierController(ModifierRepository modifierRepository) {
        this.modifierRepository = modifierRepository;
    }

    @GetMapping
    List<Modifier> all() {
        return modifierRepository.findAll();
    }

    @PostMapping
    Modifier create(@Valid @RequestBody Modifier m) {
        return modifierRepository.save(m);
    }

    @GetMapping("/{id}")
    Modifier get(@PathVariable Long id) {
        return modifierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    Modifier put(@PathVariable Long id, @Valid @RequestBody Modifier m) {
        m.setId(id);
        return modifierRepository.save(m);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        modifierRepository.deleteById(id);
    }
}
