package com.bruno.misgastos.entities;

import jakarta.persistence.*;

@Entity
public class GoogleAuthToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(name="access_token")
  private final String accessToken;

  @Column(name="refresh_token")
  private final String refreshToken;

  private final Boolean revoked;

  public GoogleAuthToken() {
    this.id = null;
    this.accessToken = null;
    this.refreshToken = null;
    this.revoked = null;
  }

  public GoogleAuthToken(String accessToken, String refreshToken) {
    this.id = null;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.revoked = false;
  }
}
