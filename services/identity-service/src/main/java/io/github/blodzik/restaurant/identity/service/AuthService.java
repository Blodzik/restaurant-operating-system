package io.github.blodzik.restaurant.identity.service;

import io.github.blodzik.restaurant.identity.entity.User;
import io.github.blodzik.restaurant.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    public String authenticate(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if(!passwordService.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if(!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is disabled");
        }

        return jwtService.issueToken(user.getId(), user.getRole());
    }

    public void verifyPin(Long userId, String rawPin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if(user.getPinHash() == null || !passwordService.matches(rawPin, user.getPinHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid PIN");
        }
    }
}
