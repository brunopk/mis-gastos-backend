package com.bruno.misgastos.scheduling.tasks;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;
import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDTO;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.rest.GoogleRestClient;
import com.bruno.misgastos.utils.Constants;
import com.bruno.misgastos.utils.EncryptionUtils;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

public class GoogleTokenRefreshTask implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTokenRefreshTask.class);

  private final GoogleRestClient googleRestClient;

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  private final GoogleAuthTokenDTO tokenToBeRefreshed;

  private final SecretKey encryptionSecret;

  private final TaskScheduler taskScheduler;

  public GoogleTokenRefreshTask(
      GoogleAuthTokenDTO tokenToBeRefreshed,
      GoogleRestClient googleRestClient,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      SecretKey encryptionSecret,
      TaskScheduler taskScheduler) {
    this.tokenToBeRefreshed = tokenToBeRefreshed;
    this.googleAuthTokenRepository = googleAuthTokenRepository;
    this.googleRestClient = googleRestClient;
    this.encryptionSecret = encryptionSecret;
    this.taskScheduler = taskScheduler;
  }

  @Transactional
  public void run() {
    try {
      LOGGER.info("Refreshing Google token");
      String refreshToken =
          Objects.requireNonNullElseGet(
              tokenToBeRefreshed.refreshToken(),
              () -> {
                String encryptedRefreshToken =
                    googleAuthTokenRepository
                        .getLastActiveToken()
                        .orElseThrow(() -> new RuntimeException("No active tokens found"))
                        .getRefreshToken();
                return EncryptionUtils.decryptString(encryptionSecret, encryptedRefreshToken);
              });
      GoogleTokenResponseDTO resp =
          googleRestClient.refreshToken(new RefreshTokenRequestDTO(refreshToken));
      GoogleAuthTokenDTO newToken = saveToken(resp, encryptionSecret);
      scheduleRefreshTask(newToken, googleRestClient, googleAuthTokenRepository, encryptionSecret);
      LOGGER.info("Google token refreshed correctly");
    } catch (Exception ex) {
      LOGGER.error("Error refreshing Google token", ex);
    }
  }

  private GoogleAuthTokenDTO saveToken(GoogleTokenResponseDTO resp, SecretKey encryptionSecret) {
    String encryptedAccessToken =
        EncryptionUtils.encryptString(encryptionSecret, resp.accessToken());
    GoogleAuthToken googleAuthToken =
        new GoogleAuthToken(encryptedAccessToken, resp.refreshToken(), resp.expiresIn());
    googleAuthTokenRepository.save(googleAuthToken);
    return new GoogleAuthTokenDTO(
        resp.accessToken(), resp.refreshToken(), resp.idToken(), resp.expiresIn());
  }

  private void scheduleRefreshTask(
      GoogleAuthTokenDTO token,
      GoogleRestClient googleRestClient,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      SecretKey encryptionSecret) {
    OffsetDateTime taskTime =
        OffsetDateTime.now()
            .plusSeconds(token.expiresIn() - Constants.GOOGLE_TOKEN_EXPIRATION_TOLERANCE);
    GoogleTokenRefreshTask task =
        new GoogleTokenRefreshTask(
            token, googleRestClient, googleAuthTokenRepository, encryptionSecret, taskScheduler);
    LOGGER.debug("Scheduling token refresh task at {}", taskTime);
    taskScheduler.schedule(task, taskTime.toInstant());
  }
}
