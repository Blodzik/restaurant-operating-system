package io.github.blodzik.restaurant.order.service;

import io.github.blodzik.restaurant.order.config.RabbitMQConfig;
import io.github.blodzik.restaurant.order.entity.Guest;
import io.github.blodzik.restaurant.order.entity.OrderBatch;
import io.github.blodzik.restaurant.order.entity.OrderItem;
import io.github.blodzik.restaurant.order.entity.OrderItemStatus;
import io.github.blodzik.restaurant.order.event.BatchFireEvent;
import io.github.blodzik.restaurant.order.repository.GuestRepository;
import io.github.blodzik.restaurant.order.repository.OrderBatchRepository;
import io.github.blodzik.restaurant.order.repository.OrderItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderBatchRepository batchRepository;

    @Mock
    GuestRepository guestRepository;

    @Mock
    OrderItemRepository itemRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    OrderService orderService;

    @Test
    void addItemShouldThrowExceptionWhenGuestNotFound() {
        Long guestId = 1L;
        Mockito.when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addItemToTable(
                    4L, guestId, 101L, "Classic Burger", new BigDecimal("15.00"), "KITCHEN", 1
            );
        });

        assertEquals("Active guest not found: 1", exception.getMessage());

        verifyNoInteractions(batchRepository, itemRepository);
    }

    @Test
    void addItemShouldCreateNewBatchWhenBatchNotFound() {
        Long guestId = 1L;
        Long tableId = 1L;

        Guest guest = new Guest();
        guest.setActive(true);
        Mockito.when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));

        Mockito.when(batchRepository.findByTableIdAndFiredAtIsNull(tableId)).thenReturn(Optional.empty());

        Mockito.when(batchRepository.findMaxBatchNumberForTable(tableId)).thenReturn(0);

        OrderBatch newBatch = new OrderBatch();
        newBatch.setId(99L);
        Mockito.when(batchRepository.save(any(OrderBatch.class))).thenReturn(newBatch);

        OrderItem mockSavedItem = new OrderItem();
        mockSavedItem.setId(100L);
        Mockito.when(itemRepository.save(any(OrderItem.class))).thenReturn(mockSavedItem);

        OrderItem result = orderService.addItemToTable(
                tableId, guestId, 101L, "Classic Burger", new BigDecimal("15.00"), "KITCHEN", 1
        );

        assertEquals(100L, result.getId());

        Mockito.verify(batchRepository).findMaxBatchNumberForTable(tableId);
        Mockito.verify(batchRepository).save(any(OrderBatch.class));
        Mockito.verify(itemRepository).save(any(OrderItem.class));
    }

    @Test
    void shouldAddItemWhenGuestAndOpenBatchExist() {
        Long guestId = 1L;
        Long tableId = 1L;

        Guest guest = new Guest();
        guest.setActive(true);

        OrderBatch batch = new OrderBatch();
        batch.setId(1L);

        Mockito.when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));
        Mockito.when(batchRepository.findByTableIdAndFiredAtIsNull(tableId)).thenReturn(Optional.of(batch));

        OrderItem mockSavedItem = new OrderItem();
        mockSavedItem.setId(100L);
        Mockito.when(itemRepository.save(any(OrderItem.class))).thenReturn(mockSavedItem);

        OrderItem result = orderService.addItemToTable(
                tableId, guestId, 101L, "Classic Burger", new BigDecimal("15.00"), "KITCHEN", 1
        );

        assertEquals(100L, result.getId());

        Mockito.verify(itemRepository).save(any(OrderItem.class));

        Mockito.verify(batchRepository, Mockito.never()).save(any(OrderBatch.class));
    }

    @Test
    void fireBatchShouldThrowExceptionWhenNoOpenBatchFound() {
        Long tableId = 1L;
        String waiterName = "Nazar";

        Mockito.when(batchRepository.findByTableIdAndFiredAtIsNull(tableId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.fireBatch(tableId, waiterName);
        });

        assertEquals("No open batch to fire for this table: 1", exception.getMessage());

        verifyNoInteractions(itemRepository);
        Mockito.verify(batchRepository, Mockito.never()).save(any());
    }

    @Test
    void shouldFireBatchSuccessfully() {
        Long tableId = 1L;
        String waiterName = "Alex";

        OrderBatch openBatch = new OrderBatch();
        openBatch.setId(99L);
        openBatch.setTableId(tableId);

        OrderItem pendingItem = new OrderItem();
        pendingItem.setId(100L);
        pendingItem.setBatch(openBatch);
        pendingItem.setStatus(OrderItemStatus.PENDING);

        Mockito.when(batchRepository.findByTableIdAndFiredAtIsNull(tableId))
                .thenReturn(Optional.of(openBatch));

        Mockito.when(itemRepository.findByBatch_TableId(tableId))
                .thenReturn(List.of(pendingItem));

        Mockito.when(batchRepository.save(any(OrderBatch.class))).thenReturn(openBatch);

        OrderBatch result = orderService.fireBatch(tableId, waiterName);

        assertEquals(waiterName, result.getFiredBy());
        Assertions.assertNotNull(result.getFiredAt());

        assertEquals(OrderItemStatus.QUEUED, pendingItem.getStatus());

        Mockito.verify(itemRepository).saveAll(Mockito.anyList());
        Mockito.verify(batchRepository).save(openBatch);

        Mockito.verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.ROUTING_KEY),
                any(BatchFireEvent.class)
        );
    }
}
