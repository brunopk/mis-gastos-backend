package com.bruno.misgastos;

import com.bruno.misgastos.services.google.GoogleAuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// TODO: CONTINUE Modify to redirect to http://localhost:5173 (configurable through properties) it may be the frontend or another URL

/**
 * In production-grade systems, applications commonly persist this data in a database so background jobs and scheduled
 * tasks can later invoke * Google APIs on behalf of the user without requiring an active session.
 * <br><br>
 * The Google "sub" claim is typically used as the stable external identity * instead of email, since emails may change
 * over time.
 */
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final GoogleAuthService googleAuthService;

  public OAuth2LoginSuccessHandler(GoogleAuthService googleAuthService) {
    this.googleAuthService = googleAuthService;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    googleAuthService.processOAuthLogin(authentication);
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
