package com.bruno.misgastos.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

  @Value("${mis-gastos.google.firebase-service-account-json}")
  private String FIREBASE_SERVICE_ACCOUNT_JSON;

  @PostConstruct
  public void initialize() throws IOException {
    FileInputStream serviceAccount =
        new FileInputStream(FIREBASE_SERVICE_ACCOUNT_JSON);

    FirebaseOptions options =
        FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    if (FirebaseApp.getApps().isEmpty()) {
      FirebaseApp.initializeApp(options);
    }
  }
}
