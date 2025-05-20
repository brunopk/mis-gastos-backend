package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;

public interface GoogleAuthService {
  void updateTokens(GoogleTokenRequestDTO request);
}
