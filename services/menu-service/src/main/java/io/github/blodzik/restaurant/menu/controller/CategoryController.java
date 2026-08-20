package io.github.blodzik.restaurant.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.github.blodzik.restaurant.menu.entity.Category;
import io.github.blodzik.restaurant.menu.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "List categories")
    @ApiResponse(responseCode = "200", description = "Categories returned")
    List<Category> all() {
        return categoryService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    Category create(@Valid @RequestBody Category c) {
        return categoryService.create(c);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    Category get(@PathVariable Long id) {
        return categoryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    Category update(@PathVariable Long id, @Valid @RequestBody Category c) {
        return categoryService.update(id, c)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    void delete(@PathVariable Long id) {
        if(!categoryService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
