package io.github.blodzik.restaurant.identity.controller;

import io.github.blodzik.restaurant.identity.dto.LoginRequest;
import io.github.blodzik.restaurant.identity.dto.LoginResponse;
import io.github.blodzik.restaurant.identity.dto.PinRequest;
import io.github.blodzik.restaurant.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/verify-pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyPin(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PinRequest request
            ) {
        authService.verifyPin(userId, request.pin());
    }
}
