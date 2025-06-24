package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record Oauth2CallbackRequestDTO(
    @JsonProperty("authorization_code") @NotNull(message = "authorization code cannot be null")
        String authorizationCode,
    @JsonProperty("code_verifier") @NotNull(message = "code verifier cannot be null")
        String codeVerifier) {}
