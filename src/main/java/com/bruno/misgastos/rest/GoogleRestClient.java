package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDto;
import com.bruno.misgastos.dto.rest.google.TokenDto;

public interface GoogleRestClient {
  TokenDto refreshToken(RefreshTokenRequestDto params);
}
