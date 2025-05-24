package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;
import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDTO;

public interface GoogleRestClient {
  GoogleTokenResponseDTO getToken(GoogleTokenRequestDTO params);

  GoogleTokenResponseDTO refreshToken(RefreshTokenRequestDTO params);
}
