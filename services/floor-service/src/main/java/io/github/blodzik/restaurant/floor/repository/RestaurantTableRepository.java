package io.github.blodzik.restaurant.floor.repository;

import io.github.blodzik.restaurant.floor.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}
