package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.dto.google.GoogleTokenRequestDTO;
import com.bruno.misgastos.rest.GoogleRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// TODO: implement corresponding spring services

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

  private static Logger LOGGER = LoggerFactory.getLogger(OAuth2Controller.class);

  private final GoogleRestClient googleRestClient;

  // TODO: remove this (just for testing)

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public OAuth2Controller(GoogleRestClient googleRestClient) {
    this.googleRestClient = googleRestClient;
  }

  @PostMapping("/callback")
  public ResponseEntity<?> authCallback(@RequestBody Oauth2CallbackRequestDTO request) throws JsonProcessingException {
    Map<?, ?> response =
        googleRestClient.getToken(
            new GoogleTokenRequestDTO(request.authorizationCode(), request.codeVerifier()));
    LOGGER.debug(OBJECT_MAPPER.writeValueAsString(response));
    return ResponseEntity.ok(response);
  }
}
