package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.dto.rest.google.GetTokenRequestDTO;
import com.bruno.misgastos.dto.rest.google.TokenDTO;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.rest.GoogleRestClient;
import com.bruno.misgastos.scheduling.tasks.GoogleTokenRefreshTask;
import com.bruno.misgastos.utils.EncryptionUtils;
import com.bruno.misgastos.utils.ErrorMessages;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthServiceImpl.class);

  private static final int TOKEN_EXPIRATION_TOLERANCE = 60;

  private static final String GENERIC_ERROR_MSG = "Error obtaining or saving Google tokens";

  private static final String MULTIPLE_TOKENS_ERROR_MSG = "Multiple valid tokens found";

  private static final String NO_VALID_TOKEN_FOUND = "No valid token found on database";

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private final TaskScheduler taskScheduler;

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  private final GoogleRestClient googleRestClient;

  private final String clientId;

  private final String authorizedEmail;

  private final SecretKey encryptionSecret;


  @Autowired
  public GoogleAuthServiceImpl(
      TaskScheduler taskScheduler,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      GoogleRestClient googleRestClient,
      @Value("${google.token-encryption.secret}") String encryptionSecret,
      @Value("${google.client-id}") String clientId,
      @Value("${google.authorized-email}") String authorizedEmail) {
    this.taskScheduler = taskScheduler;
    this.googleAuthTokenRepository = googleAuthTokenRepository;
    this.googleRestClient = googleRestClient;
    this.clientId = clientId;
    this.authorizedEmail = authorizedEmail;
    this.encryptionSecret = new SecretKeySpec(Base64.getDecoder().decode(encryptionSecret), "AES");
  }

  @Override
  public GoogleAuthTokenDTO getToken(GetTokenRequestDTO request) {
    LOGGER.info("Obtaining token from Google");
    TokenDTO resp = googleRestClient.getToken(request);
    return new GoogleAuthTokenDTO(
        resp.accessToken(), resp.refreshToken(), resp.idToken(), resp.expiresIn());
  }

  @Override
  @Transactional
  public void saveToken(GoogleAuthTokenDTO token) {
    try {
      String encryptedAccessToken = EncryptionUtils.encryptString(encryptionSecret, token.accessToken());
      String encryptedRefreshToken = EncryptionUtils.encryptString(encryptionSecret, token.refreshToken());
      GoogleAuthToken googleAuthToken =
          new GoogleAuthToken(encryptedAccessToken, encryptedRefreshToken, token.expiresIn());
      googleAuthTokenRepository.save(googleAuthToken);
      scheduleRefreshTask(token, googleRestClient, googleAuthTokenRepository, encryptionSecret);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorCode.INTERNAL_SERVER_ERROR,
          ErrorMessages.GENERIC_ERROR_MESSAGE,
          ex);
    }
  }

  @Override
  public boolean isValid(GoogleAuthTokenDTO token) {
    try {
      GoogleIdTokenVerifier verifier =
          new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
              .setAudience(Collections.singletonList(clientId))
              .build();
      GoogleIdToken idToken = verifier.verify(token.idToken());
      if (Objects.isNull(idToken)) {
        LOGGER.error("Null OpenID token obtained from Google");
        return false;
      }
      return idToken.getPayload().getEmail().equals(authorizedEmail);
    } catch (GeneralSecurityException | IOException ex) {
      return false;
    }
  }

  @Override
  public Credential getUserCredentials() {
    Optional<GoogleAuthToken> token = googleAuthTokenRepository.getLastActiveToken();
    if (token.isEmpty() || isExpired(token.get()))
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_AUTH_ERROR, NO_VALID_TOKEN_FOUND);

    try {
      String decryptedToken =
          EncryptionUtils.decryptString(encryptionSecret, token.get().getAccessToken());
      return new Credential(BearerToken.authorizationHeaderAccessMethod())
          .setAccessToken(decryptedToken);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_AUTH_ERROR, GENERIC_ERROR_MSG);
    }
  }

  private void scheduleRefreshTask(
      GoogleAuthTokenDTO token,
      GoogleRestClient googleRestClient,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      SecretKey encryptionSecret) {
    OffsetDateTime taskTime =
        OffsetDateTime.now().plusSeconds(token.expiresIn() - TOKEN_EXPIRATION_TOLERANCE);
    GoogleTokenRefreshTask task =
        new GoogleTokenRefreshTask(
            token, googleRestClient, googleAuthTokenRepository, encryptionSecret, taskScheduler);
    LOGGER.debug("Scheduling token refresh task at {}", taskTime);
    taskScheduler.schedule(task, taskTime.toInstant());
  }

  private boolean isExpired(GoogleAuthToken token) {
    OffsetDateTime expirationTime = token.getCreatedAt().plusSeconds(token.getExpiresIn());
    OffsetDateTime now = OffsetDateTime.now();
    return now.isBefore(expirationTime.minusSeconds(TOKEN_EXPIRATION_TOLERANCE));
  }
}
