package io.github.blodzik.restaurant.order.service;

import io.github.blodzik.restaurant.order.entity.Guest;
import io.github.blodzik.restaurant.order.entity.OrderBatch;
import io.github.blodzik.restaurant.order.entity.OrderItem;
import io.github.blodzik.restaurant.order.entity.OrderItemStatus;
import io.github.blodzik.restaurant.order.repository.GuestRepository;
import io.github.blodzik.restaurant.order.repository.OrderBatchRepository;
import io.github.blodzik.restaurant.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderBatchRepository batchRepository;
    private final GuestRepository guestRepository;
    private final OrderItemRepository itemRepository;

    @Transactional
    public OrderItem addItemToTable(Long tableId, Long guestId, Long menuItemId,
                                    String name, BigDecimal price, String destination, int quantity) {
        Guest guest = guestRepository.findById(guestId)
                .filter(Guest::getActive)
                .orElseThrow(() -> new IllegalArgumentException("Active guest not found: " + guestId));

        OrderBatch orderBatch = batchRepository.findByTableIdAndFiredAtIsNull(tableId)
                .orElseGet(() -> createNewBatch(tableId));

        OrderItem orderItem = new OrderItem();
        orderItem.setBatch(orderBatch);
        orderItem.setGuest(guest);
        orderItem.setMenuItem(menuItemId);
        orderItem.setNameSnapshot(name);
        orderItem.setPriceSnapshot(price);
        orderItem.setDestinationSnapshot(destination);
        orderItem.setQuantity(quantity);
        orderItem.setStatus(OrderItemStatus.PENDING);

        return itemRepository.save(orderItem);
    }

    @Transactional
    public OrderBatch fireBatch(Long tableId, String waiterName) {
        OrderBatch openBatch = batchRepository.findByTableIdAndFiredAtIsNull(tableId)
                .orElseThrow(() -> new IllegalStateException("No open batch to fire for this table: " + tableId));

        openBatch.setFiredAt(LocalDateTime.now());
        openBatch.setFiredBy(waiterName);

        List<OrderItem> items = itemRepository.findByBatch_TableId(tableId).stream()
                .filter(item -> item.getBatch().getId().equals(openBatch.getId()))
                .toList();

        items.forEach(item -> item.setStatus(OrderItemStatus.QUEUED));
        itemRepository.saveAll(items);

        return batchRepository.save(openBatch);
    }


    private OrderBatch createNewBatch(Long tableId) {
        Integer nextBatchNumber = batchRepository.findMaxBatchNumberForTable(tableId);

        OrderBatch orderBatch = new OrderBatch();
        orderBatch.setTableId(tableId);
        orderBatch.setBatchNumber(nextBatchNumber);

        return batchRepository.save(orderBatch);
    }
}
