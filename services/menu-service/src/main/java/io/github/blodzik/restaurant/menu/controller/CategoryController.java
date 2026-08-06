package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.Category;
import io.github.blodzik.restaurant.menu.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    List<Category> all() {
        return categoryRepository.findAll();
    }

    @PostMapping
    Category create(@Valid @RequestBody Category c) {
        return categoryRepository.save(c);
    }

    @GetMapping("/{id}")
    Category get(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    Category update(@PathVariable Long id, @Valid @RequestBody Category c) {
        c.setId(id);
        return categoryRepository.save(c);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }


}
