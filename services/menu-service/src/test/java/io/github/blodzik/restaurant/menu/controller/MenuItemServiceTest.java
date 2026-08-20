package io.github.blodzik.restaurant.menu.controller;

import io.github.blodzik.restaurant.menu.entity.MenuItem;
import io.github.blodzik.restaurant.menu.repository.MenuItemRepository;
import io.github.blodzik.restaurant.menu.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {
    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        Mockito.when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> menuItemService.decrementStock(1L));
    }

    @Test
    void shouldReturnTrueWhenStockIsUntracked() {
        MenuItem untrackedItem = new MenuItem();
        untrackedItem.setTrackStock(false);

        Mockito.when(menuItemRepository.findById(1L)).thenReturn(Optional.of(untrackedItem));

        boolean result = menuItemService.decrementStock(1L);

        assertTrue(result);
        Mockito.verify(menuItemRepository, Mockito.never()).decrementStock(Mockito.anyLong());
    }

    @Test
    void shouldReturnTrueWhenStockDecrementSucceeds() {
        MenuItem trackedItem = new MenuItem();
        trackedItem.setTrackStock(true);

        Mockito.when(menuItemRepository.findById(1L)).thenReturn(Optional.of(trackedItem));
        Mockito.when(menuItemRepository.decrementStock(1L)).thenReturn(1);

        boolean result = menuItemService.decrementStock(1L);
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenStockDecrementFalse() {
        MenuItem trackedItem = new MenuItem();
        trackedItem.setTrackStock(true);

        Mockito.when(menuItemRepository.findById(1L)).thenReturn(Optional.of(trackedItem));
        Mockito.when(menuItemRepository.decrementStock(1L)).thenReturn(0);

        boolean result = menuItemService.decrementStock(1L);
        assertFalse(result);
    }
}
