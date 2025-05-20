package com.bruno.misgastos.scheduling.tasks;

import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.services.GoogleAuthService;
import com.bruno.misgastos.utils.EncryptionUtils;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

// TODO: if the scheduled task fails due to credentials error , the whole application should catch the error and finish

// TODO: continue
public class GoogleTokenRefreshTask implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTokenRefreshTask.class);

  private final SecretKey encryptionSecret;

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  private final GoogleAuthToken tokenToBeRefreshed;

  public GoogleTokenRefreshTask(
      GoogleAuthToken tokenToBeRefreshed,
      SecretKey encryptionSecret,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    this.tokenToBeRefreshed = tokenToBeRefreshed;
    this.encryptionSecret = encryptionSecret;
    this.googleAuthTokenRepository = googleAuthTokenRepository;
  }

  @Override
  public void run() {
    try {
      LOGGER.info("Refreshing Google token");
      List<GoogleAuthToken> tokenList =
        googleAuthTokenRepository.findByRevokedOrderByCreatedAtDesc(false);
      LOGGER.info("Access token: {}", tokenList.get(0).getAccessToken());
    } catch (Exception ex) {
      LOGGER.error("Error", ex);
    }
  }
}
