package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackResponseDTO;
import com.bruno.misgastos.rest.GoogleRestClient;
import com.bruno.misgastos.services.OAuth2Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

  private final OAuth2Service oAuth2Service;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public OAuth2Controller(OAuth2Service oAuth2Service) {
    this.oAuth2Service = oAuth2Service;
  }

  @PostMapping("/callback")
  public ResponseEntity<Oauth2CallbackResponseDTO> authCallback(@RequestBody Oauth2CallbackRequestDTO request) {
    Oauth2CallbackResponseDTO response = oAuth2Service.authCallback(request);
    return ResponseEntity.ok(response);
  }
}
