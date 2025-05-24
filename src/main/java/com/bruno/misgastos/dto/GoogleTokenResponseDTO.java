package com.bruno.misgastos.dto;

// TODO: move this to com.bruno.misgastos.dto.rest.google and rename to GetTokenResponseDTO

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenResponseDTO(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") long expiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    String scope,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("id_token") String idToken) {}
