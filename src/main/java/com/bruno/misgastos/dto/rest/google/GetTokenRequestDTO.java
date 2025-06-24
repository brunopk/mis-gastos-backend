package com.bruno.misgastos.dto.rest.google;

public record GetTokenRequestDTO(String authorizationCode, String codeVerifier) {}
