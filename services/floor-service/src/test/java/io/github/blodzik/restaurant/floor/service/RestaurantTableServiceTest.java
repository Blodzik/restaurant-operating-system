package io.github.blodzik.restaurant.floor.service;

import io.github.blodzik.restaurant.floor.entity.RestaurantTable;
import io.github.blodzik.restaurant.floor.entity.TableState;
import io.github.blodzik.restaurant.floor.repository.RestaurantTableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantTableServiceTest {
    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @InjectMocks
    private RestaurantTableService restaurantTableService;

    @Test
    void shouldThrowExceptionWhenTableIdNotFound() {
        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restaurantTableService.seat(1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldSeatTableWhenTableExistsAndFree() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.FREE);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        Mockito.when(restaurantTableRepository.save(Mockito.any(RestaurantTable.class))).thenReturn(table);

        RestaurantTable result = restaurantTableService.seat(1L);

        assertEquals(TableState.SEATED, result.getTableState());
        assertNotNull(result.getOccupiedSince(), "Timestamp should be set when sitting");

        Mockito.verify(restaurantTableRepository).save(table);
    }

    @Test
    void shouldThrowExceptionWhenTableIsSeated() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.SEATED);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restaurantTableService.seat(1L)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }


    @Test
    void shouldRequestBillWhenTableIsSeated() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.SEATED);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        Mockito.when(restaurantTableRepository.save(Mockito.any(RestaurantTable.class))).thenReturn(table);

        RestaurantTable result = restaurantTableService.requestBill(1L);

        assertEquals(TableState.AWAITING_BILL, result.getTableState());
        Mockito.verify(restaurantTableRepository).save(table);
    }

    @Test
    void shouldThrowExceptionWhenRequestingBillForNonSeatedTable() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.FREE);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restaurantTableService.requestBill(1L)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void shouldMarkDirtyWhenTableIsAwaitingBill() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.AWAITING_BILL);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        Mockito.when(restaurantTableRepository.save(Mockito.any(RestaurantTable.class))).thenReturn(table);

        RestaurantTable result = restaurantTableService.markDirty(1L);

        assertEquals(TableState.DIRTY, result.getTableState());
        Mockito.verify(restaurantTableRepository).save(table);
    }

    @Test
    void shouldThrowExceptionWhenMarkingFreeTableDirty() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.FREE);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restaurantTableService.markDirty(1L)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void shouldCleanWhenTableIsDirty() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.DIRTY);
        table.setOccupiedSince(LocalDateTime.now());

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        Mockito.when(restaurantTableRepository.save(Mockito.any(RestaurantTable.class))).thenReturn(table);

        RestaurantTable result = restaurantTableService.clean(1L);

        assertEquals(TableState.FREE, result.getTableState());
        assertNull(result.getOccupiedSince(), "Timestamp should be cleared when cleaned");
        Mockito.verify(restaurantTableRepository).save(table);
    }

    @Test
    void shouldThrowExceptionWhenCleaningNonDirtyTable() {
        RestaurantTable table = new RestaurantTable();
        table.setTableState(TableState.SEATED);

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restaurantTableService.clean(1L)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

}
