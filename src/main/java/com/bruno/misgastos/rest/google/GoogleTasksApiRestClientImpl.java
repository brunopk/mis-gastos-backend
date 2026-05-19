package com.bruno.misgastos.rest.google;

import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;
import com.bruno.misgastos.exceptions.RestClientException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleTasksApiRestClientImpl implements GoogleTasksApiRestClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTasksApiRestClientImpl.class);

  private static final String BASE_URL = "https://tasks.googleapis.com";

  private static final String LIST_TASKS_LIST_URL = "/tasks/v1/users/@me/lists";

  private final RestClient restClient;

  @Autowired
  public GoogleTasksApiRestClientImpl(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.baseUrl(BASE_URL).build();
  }

  @Override
  public ListDto<TaskListDto> listTaskLists(String accessToken) {
    LOGGER.debug("Sending GET request to {}/{}", BASE_URL, LIST_TASKS_LIST_URL);
    return restClient
      .get()
      .uri(LIST_TASKS_LIST_URL)
      .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        (request, response) -> {
          String responseBody =
            new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
          throw new RestClientException(BASE_URL, response.getStatusCode(), responseBody);
        })
      .body(new ParameterizedTypeReference<>() {});
  }
}
