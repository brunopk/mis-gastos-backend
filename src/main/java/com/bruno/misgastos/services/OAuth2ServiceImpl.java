package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackResponseDTO;
import com.bruno.misgastos.rest.GoogleRestClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

  private final GoogleAuthService googleAuthService;

  private final long jwtExpiration;

  private final SecretKey jwtSecret;

  @Autowired
  public OAuth2ServiceImpl(
      @Value("${jwt.secret}") String jwtSecret,
      @Value("${jwt.expiration}") long jwtExpiration,
      GoogleAuthService googleAuthService) {
    this.jwtSecret = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    this.jwtExpiration = jwtExpiration;
    this.googleAuthService = googleAuthService;
  }

  @Override
  public Oauth2CallbackResponseDTO authCallback(Oauth2CallbackRequestDTO request) {
    String authorizationCode = request.authorizationCode();
    String codeVerifier = request.codeVerifier();
    googleAuthService.updateTokens(new GoogleTokenRequestDTO(authorizationCode, codeVerifier));

    Date now = new Date();
    Date expirationTime = new Date(System.currentTimeMillis() + jwtExpiration * 60 * 1000);
    String token =
        Jwts.builder().setIssuedAt(now).setExpiration(expirationTime).signWith(jwtSecret).compact();

    return new Oauth2CallbackResponseDTO(token);
  }
}
