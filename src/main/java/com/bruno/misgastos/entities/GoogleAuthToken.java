package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class GoogleAuthToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(name="access_token")
  private final String accessToken;

  @Column(name="refresh_token")
  private final String refreshToken;

  @Column(name="expires_in")
  private final Long expiresIn;

  private final Boolean revoked;

  @Column(name="created_at")
  private final OffsetDateTime createdAt;

  public GoogleAuthToken() {
    this.id = null;
    this.accessToken = null;
    this.refreshToken = null;
    this.expiresIn = null;
    this.revoked = null;
    this.createdAt = null;
  }

  public GoogleAuthToken(String accessToken, String refreshToken, Long expiresIn) {
    this.id = null;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.revoked = false;
    this.createdAt = OffsetDateTime.now();
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Long getExpiresIn() {
    return expiresIn;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
