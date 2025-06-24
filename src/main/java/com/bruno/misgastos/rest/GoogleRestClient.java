package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.rest.google.GetTokenRequestDTO;
import com.bruno.misgastos.dto.rest.google.GetTokenResponseDTO;
import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDTO;

public interface GoogleRestClient {
  GetTokenResponseDTO getToken(GetTokenRequestDTO params);

  GetTokenResponseDTO refreshToken(RefreshTokenRequestDTO params);
}
