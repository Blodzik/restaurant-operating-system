package io.github.blodzik.restaurant.menu.repository;

import io.github.blodzik.restaurant.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
