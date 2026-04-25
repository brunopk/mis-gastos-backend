package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.rest.google.RefreshTokenRequestDto;
import com.bruno.misgastos.dto.rest.google.TokenDto;
import com.bruno.misgastos.exceptions.RestClientException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class GoogleRestClientImpl implements GoogleRestClient {

  private static final String BASE_URL = "https://oauth2.googleapis.com";

  private final RestClient.Builder restClientBuilder;

  @Autowired
  public GoogleRestClientImpl(RestClient.Builder restClientBuilder) {
    this.restClientBuilder = restClientBuilder;
  }

  @Override
  public TokenDto refreshToken(RefreshTokenRequestDto params) {
    RestClient restClient = restClientBuilder.build();

    // TODO: Investigate how to obtain tokens and refresh tokens from already logged users. Try to use Spring libraries for Google.
    String clientId = "xxxx";
    String clientSecret = "xxxxxx";

    String url = String.format("%s/token", BASE_URL);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "refresh_token");
    body.add("client_id", clientId);
    body.add("client_secret", clientSecret);
    body.add("refresh_token", params.refreshToken());

    return restClient
      .post()
      .uri(url)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString())
      .body(body)
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        (request, response) -> {
          String responseBody =
            new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
          throw new RestClientException(BASE_URL, response.getStatusCode(), responseBody);
        })
      .body(TokenDto.class);
  }
}
