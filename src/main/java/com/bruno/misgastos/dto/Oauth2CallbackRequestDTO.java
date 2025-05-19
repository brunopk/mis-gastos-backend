package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: validate params

public record Oauth2CallbackRequestDTO(
    @JsonProperty("authorization_code") String authorizationCode,
    @JsonProperty("code_verifier") String codeVerifier) {}
