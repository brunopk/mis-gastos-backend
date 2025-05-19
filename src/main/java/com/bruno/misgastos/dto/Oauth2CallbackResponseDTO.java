package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Oauth2CallbackResponseDTO(@JsonProperty("access_token") String accessToken) {}
