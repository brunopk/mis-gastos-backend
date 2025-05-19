package com.bruno.misgastos.dto.google;

public record GoogleTokenRequestDTO(String authorizationCode, String codeVerifier) {}
