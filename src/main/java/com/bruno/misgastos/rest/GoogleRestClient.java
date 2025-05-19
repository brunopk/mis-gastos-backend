package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;

import java.util.Map;

public interface GoogleRestClient {
  Map<?, ?> getToken(GoogleTokenRequestDTO params);
}
