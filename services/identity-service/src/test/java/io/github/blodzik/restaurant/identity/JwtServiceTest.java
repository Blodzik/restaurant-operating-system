package io.github.blodzik.restaurant.identity;

import io.github.blodzik.restaurant.identity.entity.Role;
import io.github.blodzik.restaurant.identity.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "this-is-a-super-long-secret-key-that-has-more-than-32-characters-for-safety",
            200000
    );

    @Test
    void issuedTokenParsesBackToSameUserIdAndRole() {
        String token = jwtService.issueToken(1L, Role.MANAGER);
        Jws<Claims> parsed = jwtService.parseToken(token);
        assertThat(parsed.getPayload().getSubject()).isEqualTo("1");
        assertThat(parsed.getPayload().get("role", String.class)).isEqualTo("MANAGER");
    }

    @Test
    void tamperedTokenFailsToParse() {
        String token = jwtService.issueToken(1L, Role.MANAGER);
        String tampered = token.substring(0, token.length() - 5) + "xxxxx";

        assertThatThrownBy(() -> jwtService.parseToken(tampered))
                .isInstanceOf(JwtException.class);
    }
}
