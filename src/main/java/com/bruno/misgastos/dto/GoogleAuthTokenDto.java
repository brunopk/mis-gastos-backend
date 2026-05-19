package com.bruno.misgastos.dto;

public record GoogleAuthTokenDto(
    String accessToken, String refreshToken, String idToken, long expiresIn) {}
