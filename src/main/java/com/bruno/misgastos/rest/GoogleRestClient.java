package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.rest.google.GetTokenRequestDto;
import com.bruno.misgastos.dto.rest.google.TokenDto;
import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDto;

public interface GoogleRestClient {
  TokenDto getToken(GetTokenRequestDto params);

  TokenDto refreshToken(RefreshTokenRequestDto params);
}
