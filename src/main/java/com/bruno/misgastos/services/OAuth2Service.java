package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.Oauth2CallbackRequestDto;
import jakarta.servlet.http.HttpServletRequest;

public interface OAuth2Service {
  void authCallback(HttpServletRequest request, Oauth2CallbackRequestDto body);
}
