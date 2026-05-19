package com.bruno.misgastos.dto.rest.google;

public record GetTokenRequestDto(String authorizationCode, String codeVerifier) {}
