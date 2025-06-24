package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.rest.google.GetTokenRequestDTO;
import com.bruno.misgastos.dto.rest.google.TokenDTO;
import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDTO;

public interface GoogleRestClient {
  TokenDTO getToken(GetTokenRequestDTO params);

  TokenDTO refreshToken(RefreshTokenRequestDTO params);
}
