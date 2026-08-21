package io.github.blodzik.restaurant.identity;

import io.github.blodzik.restaurant.identity.entity.Role;
import io.github.blodzik.restaurant.identity.entity.User;
import io.github.blodzik.restaurant.identity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class UserRepositoryIT {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.11");

    @Autowired
    UserRepository userRepository;

    @Test
    void saveAndFindUserByName() {
        User u = new User();
        u.setName("Nazar");
        u.setPasswordHash("hashed");
        u.setRole(Role.WAITER);
        u.setActive(true);

        userRepository.save(u);

        assertThat(userRepository.findByName("Nazar")).isPresent();
    }
}
