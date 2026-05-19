package com.bruno.misgastos.services.google;

import com.bruno.misgastos.dto.google.Task;
import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.UnauthorizedException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.rest.google.GoogleTasksApiRestClient;
import com.bruno.misgastos.utils.ErrorMessages;
import com.bruno.misgastos.utils.GoogleUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;

@Service
public class GoogleTasksServiceImpl implements GoogleTasksService {

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

  private final GoogleTasksApiRestClient googleTasksApiRestClient;

  @Autowired
  public GoogleTasksServiceImpl(
      OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
      GoogleTasksApiRestClient googleTasksApiRestClient) {
    this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    this.googleTasksApiRestClient = googleTasksApiRestClient;
  }

  @Override
  public ListDto<TaskListDto> listTaskLists(String principalName){
    OAuth2AuthorizedClient client =
        oAuth2AuthorizedClientService.loadAuthorizedClient("google", principalName);
    String accessToken = client.getAccessToken().getTokenValue();
    return googleTasksApiRestClient.listTaskLists(accessToken);
  }

  @Override
  public Task createTask(Task task, String taskList) {
    // TODO: Investigate how to obtain tokens from already logged users. Try to use Spring libraries for Google.
    /*Credential credential = getUserCredentials(googleAuthTokenRepository);
    Tasks.TasksOperations tasksOperations =
        new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build().tasks();

    // Google Tasks does not support time-based due dates
    com.google.api.services.tasks.model.Task taskAux =
        new com.google.api.services.tasks.model.Task()
            .setDue(task.due().format(DateTimeFormatter.ISO_DATE_TIME))
            .setTitle(task.title())
            .setNotes(task.notes());

    try {
      com.google.api.services.tasks.model.Task createdTask =
          tasksOperations.insert(taskList, taskAux).execute();
      return new Task(
          createdTask.getId(),
          OffsetDateTime.parse(createdTask.getDue()),
          createdTask.getTitle(),
          createdTask.getNotes());
    } catch (IOException ex) {
      throw new GoogleApiException(ex);
    }*/
    return null;
  }

  private Credential getUserCredentials(
      GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    GoogleAuthToken token =
        googleAuthTokenRepository
            .getLastActiveToken()
            .orElseThrow(
                () ->
                    new UnauthorizedException(
                        ErrorCode.UNAUTHORIZED, ErrorMessages.NO_VALID_TOKEN_FOUND));
    return GoogleUtils.getUserCredentials(token, null);
  }
}
