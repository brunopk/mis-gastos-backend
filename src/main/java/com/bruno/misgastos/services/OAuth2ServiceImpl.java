package com.bruno.misgastos.services;

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

  // TODO: CONTINUE implement obtaining saving (in a new database entity) and refreshing Google tokens

  // TODO: uncomment
  // private final GoogleRestClient googleRestClient;
  
  @Value("${jwt.secret}")
  private String SECRET;

  @Value("${jwt.expiration}")
  private long EXPIRATION;

  @Autowired
  public OAuth2ServiceImpl(GoogleRestClient googleRestClient) {
    // this.googleRestClient = googleRestClient;
  }

  @Override
  public Oauth2CallbackResponseDTO authCallback(Oauth2CallbackRequestDTO request) {
    // TODO: uncomment
    
    /*Map<?, ?> response =
    googleRestClient.getToken(
      new GoogleTokenRequestDTO(request.authorizationCode(), request.codeVerifier()));*/
    
    Base64.Decoder base64Decoder = Base64.getDecoder();
    SecretKey secretKey = Keys.hmacShaKeyFor(base64Decoder.decode(SECRET));
    Date now = new Date();
    Date expirationTime = new Date(System.currentTimeMillis() + EXPIRATION * 60 * 1000);

    String token = Jwts.builder()
      .setIssuedAt(now)
      .setExpiration(expirationTime)
      .signWith(secretKey)
      .compact();

    return  new Oauth2CallbackResponseDTO(token);
  }
}
