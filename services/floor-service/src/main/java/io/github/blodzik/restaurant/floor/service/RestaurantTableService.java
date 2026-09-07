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

@Service
@RequiredArgsConstructor
public class RestaurantTableService {
    private RestaurantTableRepository tableRepository;

    @Transactional
    public RestaurantTable seat(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(table.getTableState() != TableState.FREE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Table is not free");
        }

        table.setTableState(TableState.SEATED);
        table.setOccupiedSince(LocalDateTime.now());
        return tableRepository.save(table);
    }
}
