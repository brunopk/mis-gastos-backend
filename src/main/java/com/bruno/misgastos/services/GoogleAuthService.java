package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.google.api.client.auth.oauth2.Credential;

public interface GoogleAuthService {

  GoogleAuthTokenDTO getToken(GoogleTokenRequestDTO request);

  void saveToken(GoogleAuthTokenDTO token);

  boolean isValid(GoogleAuthTokenDTO token);

  Credential getUserCredentials();
}
