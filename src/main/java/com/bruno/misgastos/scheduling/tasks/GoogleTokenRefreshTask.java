package com.bruno.misgastos.scheduling.tasks;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: if the scheduled task fails due to credentials error , the whole application should catch
// the error and finish

// TODO: continue

public class GoogleTokenRefreshTask implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTokenRefreshTask.class);

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  private final GoogleAuthTokenDTO tokenToBeRefreshed;

  public GoogleTokenRefreshTask(
      GoogleAuthTokenDTO tokenToBeRefreshed,
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    this.tokenToBeRefreshed = tokenToBeRefreshed;
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
