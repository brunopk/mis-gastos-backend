package com.bruno.misgastos.dto;

public record GoogleTokenRequestDTO(String authorizationCode, String codeVerifier) {}
