package com.bruno.misgastos.services.google;

import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.rest.google.GoogleTasksApiRestClient;
import com.bruno.misgastos.utils.ErrorMessages;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;

// TODO: debug wich is the principal name

@Service
public class GoogleTasksServiceImpl implements GoogleTasksService {

  private static final String CANNOT_OBTAIN_GOOGLE_ACCESS_TOKEN_ERROR_MESSAGE = "Cannot obtain Google access token";

  private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

  private final GoogleTasksApiRestClient googleTasksApiRestClient;

  // TODO: just for testing , remove this

  private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

  @Autowired
  public GoogleTasksServiceImpl(
      OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
      OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
      GoogleTasksApiRestClient googleTasksApiRestClient) {
    this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    this.googleTasksApiRestClient = googleTasksApiRestClient;
    this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
  }

  @Override
  public ListDto<TaskListDto> listTaskLists(String principalName){
    String accessToken = getGoogleAccessToken(principalName);
    return googleTasksApiRestClient.listTaskLists(accessToken);
  }

  @Override
  public void createTask(String principalName, String taskListId, TaskDto task) {
    String accessToken = getGoogleAccessToken(principalName);
    googleTasksApiRestClient.createTask(accessToken, taskListId, task);
  }

  private String getGoogleAccessToken(String principalName) {
    OAuth2AuthorizeRequest request =
      OAuth2AuthorizeRequest
        .withClientRegistrationId("google")
        .principal(principalName)
        .build();

    OAuth2AuthorizedClient client =
      oAuth2AuthorizedClientManager.authorize(request);

    if (client == null) {
      throw new ApiException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorCode.GOOGLE_AUTH_ERROR,
          CANNOT_OBTAIN_GOOGLE_ACCESS_TOKEN_ERROR_MESSAGE);
    }

    return client.getAccessToken().getTokenValue();
  }
}
