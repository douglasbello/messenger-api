package br.com.douglasbello.messenger.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserInputDTO(@NotNull(message = "Username cannot be null.") @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters") String username,
                           @NotNull(message = "Password cannot be null.") @Size(min = 8, message = "Password must have at least 8 characters.") String password) {
}