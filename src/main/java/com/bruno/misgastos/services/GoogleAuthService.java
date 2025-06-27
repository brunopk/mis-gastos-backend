package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleAuthTokenDto;
import com.bruno.misgastos.dto.rest.google.GetTokenRequestDto;

public interface GoogleAuthService {

  GoogleAuthTokenDto getToken(GetTokenRequestDto request);

  void saveToken(GoogleAuthTokenDto token);

  boolean isValid(GoogleAuthTokenDto token);

}
