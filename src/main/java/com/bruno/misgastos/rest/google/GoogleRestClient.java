package com.bruno.misgastos.rest.google;

import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDto;
import com.bruno.misgastos.dto.rest.google.TokenDto;

// TODO: consider removing this class

public interface GoogleRestClient {
  TokenDto refreshToken(RefreshTokenRequestDto params);
}
