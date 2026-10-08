package io.github.blodzik.restaurant.kitchen.repository;

import io.github.blodzik.restaurant.kitchen.entity.KitchenTicketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenTicketItemRepository extends JpaRepository<KitchenTicketItem, Long> {
}
