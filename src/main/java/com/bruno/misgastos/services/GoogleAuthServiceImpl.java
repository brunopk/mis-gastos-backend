package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.rest.GoogleRestClient;
import com.bruno.misgastos.scheduling.tasks.GoogleTokenRefreshTask;
import com.bruno.misgastos.utils.EncryptionUtils;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
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

  private final TaskScheduler taskScheduler;

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  // TODO: uncomment this

  // private final GoogleRestClient googleRestClient;

  private final SecretKey secret;

  @Autowired
  public GoogleAuthServiceImpl(
      TaskScheduler taskScheduler,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      GoogleRestClient googleRestClient,
      @Value("${google.token-encryption.secret}") String secret) {
    this.taskScheduler = taskScheduler;
    this.googleAuthTokenRepository = googleAuthTokenRepository;
    // this.googleRestClient = googleRestClient;
    this.secret = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
  }

  @Override
  @Transactional
  public void updateTokens(GoogleTokenRequestDTO request) {
    try {
      LOGGER.info("Obtaining token from Google");
      // TODO: uncomment this
      // GoogleTokenResponseDTO response = googleRestClient.getToken(request);
      // TODO: remove this (hardcoded for testing)
      GoogleTokenResponseDTO resp =
          new GoogleTokenResponseDTO(
              "ya29.a0AfH6SMCf...example..",
              120,
              "1//0gSOME_REFRESH_TOKEN",
              "https://www.googleapis.com/auth/tasks",
              "Bearer",
              "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...");

      String encryptedAccessToken = EncryptionUtils.encryptString(secret, resp.accessToken());
      String encryptedRefreshToken = EncryptionUtils.encryptString(secret, resp.refreshToken());
      GoogleAuthToken googleAuthToken =
          new GoogleAuthToken(encryptedAccessToken, encryptedRefreshToken, resp.expiresIn());
      // TODO: revoke last token
      googleAuthTokenRepository.save(googleAuthToken);
      scheduleRefreshTask(taskScheduler, googleAuthToken, secret, googleAuthTokenRepository);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, GENERIC_ERROR_MSG);
    }
  }

  @Override
  public Credential getUserCredentials() {
    List<GoogleAuthToken> tokenList =
        googleAuthTokenRepository.findByRevokedOrderByCreatedAtDesc(false);

    if (tokenList.size() > 1)
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_AUTH_ERROR, MULTIPLE_TOKENS_ERROR_MSG);

    Optional<GoogleAuthToken> token = tokenList.stream().findFirst();
    if (token.isEmpty() || isExpired(token.get()))
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_AUTH_ERROR, NO_VALID_TOKEN_FOUND);

    try {
      String decryptedToken = EncryptionUtils.decryptString(secret, token.get().getAccessToken());
      return new Credential(BearerToken.authorizationHeaderAccessMethod())
          .setAccessToken(decryptedToken);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_AUTH_ERROR, GENERIC_ERROR_MSG);
    }
  }

  private boolean isExpired(GoogleAuthToken token) {
    OffsetDateTime expirationTime = token.getCreatedAt().plusSeconds(token.getExpiresIn());
    OffsetDateTime now = OffsetDateTime.now();
    return now.isBefore(expirationTime.minusSeconds(TOKEN_EXPIRATION_TOLERANCE));
  }

  private void scheduleRefreshTask(
      TaskScheduler scheduler,
      GoogleAuthToken tokenToBeRefreshed,
      SecretKey secret,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    OffsetDateTime taskTime =
        OffsetDateTime.now()
            .plusSeconds(tokenToBeRefreshed.getExpiresIn() - TOKEN_EXPIRATION_TOLERANCE);
    GoogleTokenRefreshTask task =
        new GoogleTokenRefreshTask(tokenToBeRefreshed, secret, googleAuthTokenRepository);
    LOGGER.debug("Scheduling token refresh task at {}", taskTime);
    scheduler.schedule(task, taskTime.toInstant());
  }
}
