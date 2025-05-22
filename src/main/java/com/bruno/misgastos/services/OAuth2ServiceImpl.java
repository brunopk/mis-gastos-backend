package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.GoogleAuthTokenDTO;
import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackRequestDTO;
import com.bruno.misgastos.dto.Oauth2CallbackResponseDTO;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.UnauthorizedException;
import com.bruno.misgastos.utils.ErrorMessages;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;

import jakarta.transaction.Transactional;
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
  @Transactional
  public Oauth2CallbackResponseDTO authCallback(Oauth2CallbackRequestDTO request) {
    String authorizationCode = request.authorizationCode();
    String codeVerifier = request.codeVerifier();

    GoogleAuthTokenDTO googleToken =
        googleAuthService.getToken(new GoogleTokenRequestDTO(authorizationCode, codeVerifier));
    if (!googleAuthService.isValid(googleToken))
      throw new UnauthorizedException(ErrorCode.GOOGLE_AUTH_ERROR);
    googleAuthService.saveToken(googleToken);
    googleAuthService.scheduleRefreshTask(googleToken);

    Date now = new Date();
    Date expirationTime = new Date(System.currentTimeMillis() + jwtExpiration * 60 * 1000);
    String token =
        Jwts.builder().setIssuedAt(now).setExpiration(expirationTime).signWith(jwtSecret).compact();

    return new Oauth2CallbackResponseDTO(token);
  }
}
