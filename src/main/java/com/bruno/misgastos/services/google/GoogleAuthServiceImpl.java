package com.bruno.misgastos.services.google;

import com.bruno.misgastos.dto.GoogleAuthTokenDto;
import com.bruno.misgastos.dto.rest.google.GetTokenRequestDto;
import com.bruno.misgastos.dto.rest.google.TokenDto;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.rest.GoogleRestClient;
import com.bruno.misgastos.tasks.GoogleTokenRefreshTask;
import com.bruno.misgastos.utils.EncryptionUtils;
import com.bruno.misgastos.utils.ErrorMessages;
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
  public GoogleAuthTokenDto getToken(GetTokenRequestDto request) {
    LOGGER.info("Obtaining token from Google");
    TokenDto resp = googleRestClient.getToken(request);
    return new GoogleAuthTokenDto(
        resp.accessToken(), resp.refreshToken(), resp.idToken(), resp.expiresIn());
  }

  @Override
  @Transactional
  public void saveToken(GoogleAuthTokenDto token) {
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
          ErrorMessages.GENERIC_GOOGLE_AUTH_ERROR,
          ex);
    }
  }

  @Override
  public boolean isValid(GoogleAuthTokenDto token) {
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

  private void scheduleRefreshTask(
      GoogleAuthTokenDto token,
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
}
