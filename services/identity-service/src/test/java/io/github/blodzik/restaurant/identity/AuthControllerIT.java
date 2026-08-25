package io.github.blodzik.restaurant.identity;

import io.github.blodzik.restaurant.identity.entity.Role;
import io.github.blodzik.restaurant.identity.entity.User;
import io.github.blodzik.restaurant.identity.repository.UserRepository;
import io.github.blodzik.restaurant.identity.service.JwtService;
import io.github.blodzik.restaurant.identity.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class AuthControllerIT {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.11");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordService passwordService;

    @Autowired
    JwtService jwtService;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setName("admin");
        user.setPasswordHash(passwordService.hash("secret123"));
        user.setPinHash(passwordService.hash("1234"));
        user.setRole(Role.MANAGER);
        user.setActive(true);
        userRepository.save(user);
    }

    @Test
    void loginWithCorrectCredentialsReturnsToken() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "admin",
                            "password": "secret123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginWithMissingFieldsFailsValidation() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "admin"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyPinWithValidTokenAndCorrectPinReturns204() throws Exception {
        User user = userRepository.findByName("admin").orElseThrow();

        String token = jwtService.issueToken(user.getId(), user.getRole());

        mockMvc.perform(post("/auth/verify-pin")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "pin": "1234"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    void verifyPinWithValidTokenButWrongPinReturns401() throws Exception {
        User user = userRepository.findByName("admin").orElseThrow();
        String token = jwtService.issueToken(user.getId(), user.getRole());

        mockMvc.perform(post("/auth/verify-pin")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "pin": "9999"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void verifyPinWithoutTokenReturns403() throws Exception {
        mockMvc.perform(post("/auth/verify-pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "pin": "1234"
                        }
                        """))
                .andExpect(status().isForbidden());
    }
}
