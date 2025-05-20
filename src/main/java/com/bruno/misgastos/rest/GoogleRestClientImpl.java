package com.bruno.misgastos.rest;

import com.bruno.misgastos.dto.GoogleTokenRequestDTO;
import com.bruno.misgastos.dto.GoogleTokenResponseDTO;
import com.bruno.misgastos.exceptions.RestClientException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

// TODO: get client id and redirect URI and client secret from properties ( to test just hardcode
// them)

@Component
public class GoogleRestClientImpl implements GoogleRestClient {

  private final RestClient.Builder restClientBuilder;

  private static final String BASE_URL = "https://oauth2.googleapis.com/token";

  @Autowired
  public GoogleRestClientImpl(RestClient.Builder restClientBuilder) {
    this.restClientBuilder = restClientBuilder;
  }

  @Override
  public GoogleTokenResponseDTO getToken(GoogleTokenRequestDTO params) {
    RestClient restClient = restClientBuilder.build();
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", "asd");
    body.add("client_secret", "asd");
    body.add("code", params.authorizationCode());
    body.add("code_verifier", params.codeVerifier());
    body.add("redirect_uri", "http://localhost:5173/login");

    return restClient
        .post()
        .uri(BASE_URL)
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
        .body(GoogleTokenResponseDTO.class);
  }
}
