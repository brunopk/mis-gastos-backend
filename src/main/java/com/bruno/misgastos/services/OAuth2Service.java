package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackResponseDTO;

public interface OAuth2Service {
  Oauth2CallbackResponseDTO authCallback(Oauth2CallbackRequestDTO request);
}
