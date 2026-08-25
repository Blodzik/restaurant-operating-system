package io.github.blodzik.restaurant.identity.bootstrap;

import io.github.blodzik.restaurant.identity.entity.Role;
import io.github.blodzik.restaurant.identity.entity.User;
import io.github.blodzik.restaurant.identity.repository.UserRepository;
import io.github.blodzik.restaurant.identity.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    public void run(String... args) {
        if(userRepository.count() == 0) {
            User manager = new User();
            manager.setName("admin");
            manager.setPasswordHash(passwordService.hash("changeme"));
            manager.setRole(Role.MANAGER);
            manager.setActive(true);

            userRepository.save(manager);
            System.out.println("Seeded default admin user");
        }
    }
}
