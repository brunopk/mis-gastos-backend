package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;
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

// TODO: after generating new one, validate with an external service or something the last token in database is invalid

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

  private final SecretKey secret;


  @Autowired
  public GoogleAuthServiceImpl(
      TaskScheduler taskScheduler,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      GoogleRestClient googleRestClient,
      @Value("${google.token-encryption.secret}") String secret,
      @Value("${google.client-id}") String clientId,
      @Value("${google.authorized-email}") String authorizedEmail) {
    this.taskScheduler = taskScheduler;
    this.googleAuthTokenRepository = googleAuthTokenRepository;
    this.googleRestClient = googleRestClient;
    this.clientId = clientId;
    this.authorizedEmail = authorizedEmail;
    this.secret = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
  }

  @Override
  public GoogleAuthTokenDTO getToken(GoogleTokenRequestDTO request) {
    LOGGER.info("Obtaining token from Google");
    GoogleTokenResponseDTO resp = googleRestClient.getToken(request);
    return new GoogleAuthTokenDTO(
        resp.accessToken(), resp.refreshToken(), resp.idToken(), resp.expiresIn(), false);
  }

  @Override
  public void saveToken(GoogleAuthTokenDTO token) {
    try {
      String encryptedAccessToken = EncryptionUtils.encryptString(secret, token.accessToken());
      String encryptedRefreshToken = EncryptionUtils.encryptString(secret, token.refreshToken());
      GoogleAuthToken googleAuthToken =
          new GoogleAuthToken(encryptedAccessToken, encryptedRefreshToken, token.expiresIn());
      // TODO: revoke last token
      googleAuthTokenRepository.save(googleAuthToken);

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
  public void scheduleRefreshTask(GoogleAuthTokenDTO token) {
    OffsetDateTime taskTime =
        OffsetDateTime.now().plusSeconds(token.expiresIn() - TOKEN_EXPIRATION_TOLERANCE);
    GoogleTokenRefreshTask task = new GoogleTokenRefreshTask(token, googleAuthTokenRepository);
    LOGGER.debug("Scheduling token refresh task at {}", taskTime);
    taskScheduler.schedule(task, taskTime.toInstant());
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
}
