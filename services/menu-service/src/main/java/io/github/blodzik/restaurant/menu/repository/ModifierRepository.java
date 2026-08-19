package io.github.blodzik.restaurant.menu.repository;

import io.github.blodzik.restaurant.menu.entity.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModifierRepository extends JpaRepository<Modifier, Long> {
}
