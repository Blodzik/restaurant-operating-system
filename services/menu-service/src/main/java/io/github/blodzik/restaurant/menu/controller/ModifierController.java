package io.github.blodzik.restaurant.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Modifiers", description = "Modifier management endpoints")
public class ModifierController {
    private final ModifierService modifierService;

    @GetMapping
    @Operation(summary = "List modifiers")
    @ApiResponse(responseCode = "200", description = "Modifiers returned")
    List<Modifier> all() {
        return modifierService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get modifier by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modifier found"),
            @ApiResponse(responseCode = "404", description = "Modifier not found")
    })
    Modifier get(@PathVariable Long id) {
        return modifierService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Create modifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modifier created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    Modifier create(@Valid @RequestBody Modifier m) {
        return modifierService.create(m);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update modifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modifier updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Modifier not found")
    })
    Modifier put(@PathVariable Long id, @Valid @RequestBody Modifier m) {
        return modifierService.update(id, m)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete modifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modifier deleted"),
            @ApiResponse(responseCode = "404", description = "Modifier not found")
    })
    void delete(@PathVariable Long id) {
        if(!modifierService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
