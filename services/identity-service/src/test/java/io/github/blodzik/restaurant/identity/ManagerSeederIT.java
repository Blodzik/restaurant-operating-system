package io.github.blodzik.restaurant.identity;

import io.github.blodzik.restaurant.identity.entity.Role;
import io.github.blodzik.restaurant.identity.entity.User;
import io.github.blodzik.restaurant.identity.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@SpringBootTest
@Testcontainers
public class ManagerSeederIT {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.11");

    @Autowired
    UserRepository userRepository;

    @Test
    void seedsDefaultManagerOnStartUp() {
        Optional<User> admin = userRepository.findByName("admin");

        Assertions.assertThat(admin).isPresent();
        Assertions.assertThat(admin.get().getRole()).isEqualTo(Role.MANAGER);
        Assertions.assertThat(admin.get().isActive()).isTrue();
    }
}
