package io.github.blodzik.restaurant.menu.repository;

import io.github.blodzik.restaurant.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Modifying
    @Query("UPDATE MenuItem m SET m.stockCount = m.stockCount - 1 WHERE m.id = :id AND m.stockCount >= 1")
    int decrementStock(@Param("id") Long id);
}
