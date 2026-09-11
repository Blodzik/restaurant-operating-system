package io.github.blodzik.restaurant.floor.service;

import io.github.blodzik.restaurant.floor.entity.RestaurantTable;
import io.github.blodzik.restaurant.floor.entity.TableState;
import io.github.blodzik.restaurant.floor.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTableService {
    private final RestaurantTableRepository tableRepository;

    public List<RestaurantTable> findAll() {
        return tableRepository.findAll();
    }

    @Transactional
    public RestaurantTable seat(Long tableId) {
        RestaurantTable table = getTableOrThrow(tableId);

        if(table.getTableState() != TableState.FREE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Table is not free");
        }

        table.setTableState(TableState.SEATED);
        table.setOccupiedSince(LocalDateTime.now());
        return tableRepository.save(table);
    }

    @Transactional
    public RestaurantTable requestBill(Long tableId) {
        RestaurantTable table = getTableOrThrow(tableId);

        if(table.getTableState() != TableState.SEATED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Table must be seated to request a bill");
        }

        table.setTableState(TableState.AWAITING_BILL);
        return tableRepository.save(table);
    }

    @Transactional
    public RestaurantTable markDirty(Long tableId) {
        RestaurantTable table = getTableOrThrow(tableId);

        if(table.getTableState() == TableState.FREE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Table is already FREE");
        } else if (table.getTableState() == TableState.DIRTY) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Table is already DIRTY");
        }

        table.setTableState(TableState.DIRTY);
        return tableRepository.save(table);
    }

    @Transactional
    public RestaurantTable clean(Long tableId) {
        RestaurantTable table = getTableOrThrow(tableId);

        if(table.getTableState() != TableState.DIRTY) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only DIRTY tables can be cleaned");
        }

        table.setTableState(TableState.FREE);
        table.setOccupiedSince(null);
        return tableRepository.save(table);
    }

    private RestaurantTable getTableOrThrow(Long tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
