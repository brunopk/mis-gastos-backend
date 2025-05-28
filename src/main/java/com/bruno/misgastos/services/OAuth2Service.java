package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface OAuth2Service {
  void authCallback(HttpServletRequest request, Oauth2CallbackRequestDTO body);
}
