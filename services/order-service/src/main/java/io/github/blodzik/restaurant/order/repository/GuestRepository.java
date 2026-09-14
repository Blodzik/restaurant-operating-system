package io.github.blodzik.restaurant.order.repository;

import io.github.blodzik.restaurant.order.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByTableIdAndActiveTrue(Long tableId);
}
