package io.github.blodzik.restaurant.identity.controller;

import io.github.blodzik.restaurant.identity.dto.LoginRequest;
import io.github.blodzik.restaurant.identity.dto.LoginResponse;
import io.github.blodzik.restaurant.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.name(), loginRequest.password());
        return new LoginResponse(token);
    }
}
