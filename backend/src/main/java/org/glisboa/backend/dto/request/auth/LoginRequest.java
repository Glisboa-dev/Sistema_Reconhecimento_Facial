package org.glisboa.backend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Nome de usuário é obrigatório") String username, @NotBlank(message = "Senha é obrigatória") String password) {
}
