package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.services.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

  private final OAuth2Service oAuth2Service;
  
  @Autowired
  public OAuth2Controller(OAuth2Service oAuth2Service) {
    this.oAuth2Service = oAuth2Service;
  }

  @PostMapping("/callback")
  public ResponseEntity<Object> authCallback(
      HttpServletRequest request, @RequestBody Oauth2CallbackRequestDTO body) {
    oAuth2Service.authCallback(request, body);
    return ResponseEntity.ok(Collections.emptyMap());
  }
}
