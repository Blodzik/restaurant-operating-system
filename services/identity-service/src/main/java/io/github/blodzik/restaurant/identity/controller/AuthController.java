package io.github.blodzik.restaurant.identity.controller;

import io.github.blodzik.restaurant.identity.dto.LoginRequest;
import io.github.blodzik.restaurant.identity.dto.LoginResponse;
import io.github.blodzik.restaurant.identity.dto.PinRequest;
import io.github.blodzik.restaurant.identity.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for staff login and PIN verification")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with password", description = "Returns a JWT token for standard user authentication")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.name(), loginRequest.password());
        return new LoginResponse(token);
    }

    @PostMapping("/verify-pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Verify PIN", description = "Quick verification using a 4-digit PIN for sensitive actions (Requires JWT)")
    public void verifyPin(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PinRequest request
            ) {
        authService.verifyPin(userId, request.pin());
    }
}
