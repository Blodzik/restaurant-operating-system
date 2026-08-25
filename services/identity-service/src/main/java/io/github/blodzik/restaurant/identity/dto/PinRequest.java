package io.github.blodzik.restaurant.identity.dto;

import jakarta.validation.constraints.NotBlank;

public record PinRequest(@NotBlank String pin) {
}
