package io.github.blodzik.restaurant.identity;

import io.github.blodzik.restaurant.identity.service.PasswordService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordServiceTest {
    PasswordService service = new PasswordService();

    @Test
    void hashedPasswordMatchesOriginal() {
        String hash = service.hash("secret123");
        assertThat(service.matches("secret123", hash)).isTrue();
    }

    @Test
    void wrongPasswordDoesNotMatch() {
        String hash = service.hash("secret123");
        assertThat(service.matches("secret1234", hash)).isFalse();
    }
}
