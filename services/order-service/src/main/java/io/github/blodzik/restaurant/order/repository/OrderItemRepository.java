package io.github.blodzik.restaurant.order.repository;

import io.github.blodzik.restaurant.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByBatch_TableId(Long tableId);
}
