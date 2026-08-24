package io.github.blodzik.restaurant.identity;

import io.github.blodzik.restaurant.identity.entity.Role;
import io.github.blodzik.restaurant.identity.entity.User;
import io.github.blodzik.restaurant.identity.repository.UserRepository;
import io.github.blodzik.restaurant.identity.service.AuthService;
import io.github.blodzik.restaurant.identity.service.JwtService;
import io.github.blodzik.restaurant.identity.service.PasswordService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordService passwordService;
    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @Test
    void authenticateThrowUnauthorizedWhenNotFound() {
        when(userRepository.findByName("unknown")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> authService.authenticate("unknown", "password"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status").isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void authenticateReturnsTokenWhenValid() {
        User user = new User();
        user.setId(1L);
        user.setName("Nazar");
        user.setRole(Role.WAITER);
        user.setPasswordHash("password-hash");
        user.setActive(true);

        when(userRepository.findByName("Nazar")).thenReturn(Optional.of(user));
        when(passwordService.matches("rawPassword", "password-hash")).thenReturn(true);
        when(jwtService.issueToken(1L, Role.WAITER)).thenReturn("jwt-token");

        String token = authService.authenticate("Nazar", "rawPassword");
        Assertions.assertThat(token).isEqualTo("jwt-token");
    }
}
