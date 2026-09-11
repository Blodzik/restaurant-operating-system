package io.github.blodzik.restaurant.floor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.github.blodzik.restaurant.floor.entity.RestaurantTable;
import io.github.blodzik.restaurant.floor.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
@Tag(name = "Tables", description = "Restaurant table management endpoints")
public class RestaurantTableController {
    private final RestaurantTableService tableService;

    @GetMapping
    @Operation(summary = "List tables")
    @ApiResponse(responseCode = "200", description = "Tables returned")
    public List<RestaurantTable> all() {
        return tableService.findAll();
    }

    @PostMapping("/{id}/seat")
    @Operation(summary = "Seat a table")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table seated"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "409", description = "Table is not free")
    })
    public RestaurantTable seat(@PathVariable Long id) {
        return tableService.seat(id);
    }

    @PostMapping("/{id}/request-bill")
    @Operation(summary = "Request the bill for a table")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bill requested"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "409", description = "Table must be seated to request a bill")
    })
    public RestaurantTable requestBill(@PathVariable Long id) {
        return tableService.requestBill(id);
    }

    @PostMapping("/{id}/mark-dirty")
    @Operation(summary = "Mark a table as dirty")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table marked dirty"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "409", description = "Table is already FREE or DIRTY")
    })
    public RestaurantTable markDirty(@PathVariable Long id) {
        return tableService.markDirty(id);
    }

    @PostMapping("/{id}/clear")
    @Operation(summary = "Clear a dirty table")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table cleared"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "409", description = "Only DIRTY tables can be cleaned")
    })
    public RestaurantTable clean(@PathVariable Long id) {
        return tableService.clean(id);
    }
}