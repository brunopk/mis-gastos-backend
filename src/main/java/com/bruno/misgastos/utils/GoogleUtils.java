package com.bruno.misgastos.utils;

import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import java.time.OffsetDateTime;
import javax.crypto.SecretKey;
import org.springframework.http.HttpStatus;

public interface GoogleUtils {

  /**
   * Obtain credentials required to interact with Google APIs.
   * @param token Token obtained from DB (encrypted tokens)
   * @param encryptionSecret Required to decrypt tokens
   * @return Returns a {@code Credential} instance
   * @throws ApiException Throws ApiException
   */
  static Credential getUserCredentials(GoogleAuthToken token, SecretKey encryptionSecret) {
    if (token.getAccessToken().isEmpty() || isExpired(token))
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorCode.GOOGLE_AUTH_ERROR,
          ErrorMessages.NO_VALID_TOKEN_FOUND);

    try {
      String decryptedToken =
          EncryptionUtils.decryptString(encryptionSecret, token.getAccessToken());
      return new Credential(BearerToken.authorizationHeaderAccessMethod())
          .setAccessToken(decryptedToken);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorCode.GOOGLE_AUTH_ERROR,
          ErrorMessages.GENERIC_GOOGLE_AUTH_ERROR);
    }
  }

  private static boolean isExpired(GoogleAuthToken token) {
    OffsetDateTime expirationTime = token.getCreatedAt().plusSeconds(token.getExpiresIn());
    OffsetDateTime now = OffsetDateTime.now();
    return now.isAfter(expirationTime) || now.isEqual(expirationTime);
  }
}
