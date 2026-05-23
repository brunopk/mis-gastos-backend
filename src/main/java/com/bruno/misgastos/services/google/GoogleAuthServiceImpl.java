package com.bruno.misgastos.services.google;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthServiceImpl.class);

  private static final String PRINCIPAL_NOT_FOUND = "Principal not found";

  private static final String CANNOT_OBTAIN_EMAIL_ERROR_MSG_TEMPLATE =
      "Cannot obtain email for principal %s from provider %s";

  private final Map<String, String> emailToPrincipal = new ConcurrentHashMap<>();

  @Override
  public void processOAuthLogin(Authentication authentication) {
    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    OAuth2User user = oauthToken.getPrincipal();

    String principalName = authentication.getName();
    String email = user.getAttribute("email");

    if (email == null) {
      String errorMsg =
          String.format(
              CANNOT_OBTAIN_EMAIL_ERROR_MSG_TEMPLATE,
              principalName,
              ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId());
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, errorMsg);
    }
    emailToPrincipal.put(email, principalName);

    LOGGER.info("Stored mapping: {} -> {}", email, principalName);
  }

  @Override
  public String getPrincipalByEmail(String email) {
    String principal = emailToPrincipal.get(email);
    if (principal == null) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, PRINCIPAL_NOT_FOUND);
    }
    return principal;
  }
}
