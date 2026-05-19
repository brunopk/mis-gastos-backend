package com.bruno.misgastos.dto.rest.google;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: consider removing this class

public record RefreshTokenRequestDto(@JsonProperty("refresh_token") String refreshToken) {}
