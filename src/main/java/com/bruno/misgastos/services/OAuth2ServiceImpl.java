package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServiceImpl.class);

  private final GoogleAuthService googleAuthService;

  @Autowired
  public OAuth2ServiceImpl(GoogleAuthService googleAuthService) {
    this.googleAuthService = googleAuthService;
  }

  @Override
  @Transactional
  public void authCallback(HttpServletRequest request, Oauth2CallbackRequestDTO body) {
    String authorizationCode = body.authorizationCode();
    String codeVerifier = body.codeVerifier();

    GoogleAuthTokenDTO googleToken =
        googleAuthService.getToken(new GoogleTokenRequestDTO(authorizationCode, codeVerifier));
    if (!googleAuthService.isValid(googleToken))
      throw new UnauthorizedException(ErrorCode.GOOGLE_AUTH_ERROR);
    googleAuthService.saveToken(googleToken);

    // Creates a new session if it wasn't created before
    // Spring identify sessions using JSESSIONID cookie
    HttpSession session = request.getSession(true);
    if (session.isNew()) {
      LOGGER.info("New session created (ID= {})", session.getId());
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(null, null, Collections.emptyList());
      // If you only set SecurityContextHolder.getContext().setAuthentication() in the current
      // request thread, but don’t store it in the session, the authentication will be lost on the
      // next request.
      SecurityContext securityContext = SecurityContextHolder.getContext();
      securityContext.setAuthentication(authentication);
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
  }
}
