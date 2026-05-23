package com.bruno.misgastos.services.google;

import org.springframework.security.core.Authentication;

public interface GoogleAuthService {
  void processOAuthLogin(Authentication authentication);

  String getPrincipalByEmail(String email);
}
