package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.rest.GoogleRestClient;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

// TODO: CONTINUE function to retrieve tokens from database and schedule task to renew tokens

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

  private static final int TOKEN_ENCRYPTION_IV_LENGTH = 12;

  private static final int TOKEN_ENCRYPTION_TAG_LENGTH = 128;

  private static final String TOKEN_ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

  private static final String ERROR_MESSAGE = "Error obtaining or saving Google tokens";

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  // TODO: uncomment this

  // private final GoogleRestClient googleRestClient;

  private final SecretKey secret;

  @Autowired
  public GoogleAuthServiceImpl(
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository,
      GoogleRestClient googleRestClient,
      @Value("${google.token-encryption.secret}") String secret) {
    this.googleAuthTokenRepository = googleAuthTokenRepository;
    // this.googleRestClient = googleRestClient;
    this.secret = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
  }

  @Override
  public void updateTokens(GoogleTokenRequestDTO request) {
    try {
      // TODO: uncomment this
      // GoogleTokenResponseDTO response = googleRestClient.getToken(request);
      // TODO: remove this (hardcoded for testing)
      GoogleTokenResponseDTO resp =
          new GoogleTokenResponseDTO(
              "ya29.a0AfH6SMCf...example..",
              3599,
              "1//0gSOME_REFRESH_TOKEN",
              "https://www.googleapis.com/auth/tasks",
              "Bearer",
              "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...");
      String encryptedAccessToken = encryptToken(resp.accessToken());
      String encryptedRefreshToken = encryptToken(resp.refreshToken());
      GoogleAuthToken googleAuthToken =
          new GoogleAuthToken(encryptedAccessToken, encryptedRefreshToken);
      googleAuthTokenRepository.save(googleAuthToken);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
    }
  }

  private String encryptToken(String token) throws Exception {
    // IV stands for Initialization Vector. It's used to enhance the security of stored tokens.
    byte[] iv = new byte[TOKEN_ENCRYPTION_IV_LENGTH];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(iv);

    Cipher cipher = Cipher.getInstance(TOKEN_ENCRYPTION_ALGORITHM);
    GCMParameterSpec gcmSpec = new GCMParameterSpec(TOKEN_ENCRYPTION_TAG_LENGTH, iv);
    cipher.init(Cipher.ENCRYPT_MODE, secret, gcmSpec);

    byte[] encryptedBytes = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));

    // Prepend IV to ciphertext
    byte[] ivAndEncrypted = new byte[iv.length + encryptedBytes.length];
    System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
    System.arraycopy(encryptedBytes, 0, ivAndEncrypted, iv.length, encryptedBytes.length);

    return Base64.getEncoder().encodeToString(ivAndEncrypted);
  }
}
