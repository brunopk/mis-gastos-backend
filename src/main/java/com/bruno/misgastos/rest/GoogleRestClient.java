package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;

public interface GoogleRestClient {
  GoogleTokenResponseDTO getToken(GoogleTokenRequestDTO params);
}
