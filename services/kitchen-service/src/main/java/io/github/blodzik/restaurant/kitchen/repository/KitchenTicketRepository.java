package io.github.blodzik.restaurant.kitchen.repository;

import io.github.blodzik.restaurant.kitchen.entity.KitchenTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenTicketRepository extends JpaRepository<KitchenTicket, Long> {
}
