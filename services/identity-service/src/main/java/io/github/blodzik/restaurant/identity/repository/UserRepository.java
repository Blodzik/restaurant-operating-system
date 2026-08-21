package io.github.blodzik.restaurant.identity.repository;

import io.github.blodzik.restaurant.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
