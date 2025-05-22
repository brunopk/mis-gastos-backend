package com.bruno.misgastos.dto;

public record GoogleAuthTokenDTO(
    String accessToken, String refreshToken, String idToken, long expiresIn, boolean revoked) {}
