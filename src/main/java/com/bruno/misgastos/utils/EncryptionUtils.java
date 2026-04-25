package com.bruno.misgastos.utils;

import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.springframework.http.HttpStatus;

public interface EncryptionUtils {
  int IV_LENGTH = 12;

  int TAG_LENGTH = 128;

  String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

  static String encryptString(SecretKey secret, String plainText) {
    try {
      // IV stands for Initialization Vector. It's used to enhance the security of stored tokens.
      byte[] iv = new byte[IV_LENGTH];
      SecureRandom secureRandom = new SecureRandom();
      secureRandom.nextBytes(iv);

      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
      GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
      cipher.init(Cipher.ENCRYPT_MODE, secret, gcmSpec);

      byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

      // Prepend IV to ciphertext
      byte[] ivAndEncrypted = new byte[iv.length + encryptedBytes.length];
      System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
      System.arraycopy(encryptedBytes, 0, ivAndEncrypted, iv.length, encryptedBytes.length);

      return Base64.getEncoder().encodeToString(ivAndEncrypted);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ENCRYPTION_ERROR, ex.getMessage());
    }
  }

  static String decryptString(SecretKey secret, String encryptedText) {
    try {
      byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedText);
      byte[] iv = new byte[IV_LENGTH];
      byte[] encryptedBytes = new byte[ivAndEncrypted.length - IV_LENGTH];

      System.arraycopy(ivAndEncrypted, 0, iv, 0, IV_LENGTH);
      System.arraycopy(ivAndEncrypted, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
      GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
      cipher.init(Cipher.DECRYPT_MODE, secret, gcmSpec);

      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

      return new String(decryptedBytes, StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.ENCRYPTION_ERROR, ex.getMessage());
    }
  }
}
