package com.bruno.misgastos.dto;

// TODO: move this to com.bruno.misgastos.dto.rest.google and rename to GetTokenRequestDTO

public record GoogleTokenRequestDTO(String authorizationCode, String codeVerifier) {}
