package io.github.blodzik.restaurant.floor.controller;

import io.github.blodzik.restaurant.floor.entity.RestaurantTable;
import io.github.blodzik.restaurant.floor.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class RestaurantTableController {
    private final RestaurantTableService tableService;

    @GetMapping
    public List<RestaurantTable> all() {
        return tableService.findAll();
    }

    @PostMapping("/{id}/seat")
    public RestaurantTable seat(@PathVariable Long id) {
        return tableService.seat(id);
    }
}
