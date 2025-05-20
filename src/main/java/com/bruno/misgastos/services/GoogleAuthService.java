package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.google.api.client.auth.oauth2.Credential;

public interface GoogleAuthService {
  void updateTokens(GoogleTokenRequestDTO request);

  Credential getUserCredentials();
}
