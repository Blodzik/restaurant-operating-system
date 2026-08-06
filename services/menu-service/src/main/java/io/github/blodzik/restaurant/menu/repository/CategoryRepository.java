package io.github.blodzik.restaurant.menu.repository;

import io.github.blodzik.restaurant.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
