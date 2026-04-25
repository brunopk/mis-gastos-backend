package com.bruno.misgastos.services.google;

import com.bruno.misgastos.dto.google.Task;
import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.UnauthorizedException;
import com.bruno.misgastos.exceptions.google.GoogleApiException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.utils.ErrorMessages;
import com.bruno.misgastos.utils.GoogleUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleTaskServiceImpl implements GoogleTaskService {

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  @Autowired
  public GoogleTaskServiceImpl(GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    this.googleAuthTokenRepository = googleAuthTokenRepository;
  }

  @Override
  public void test() throws IOException {
    // TODO: Investigate how to obtain tokens from already logged users. Try to use Spring libraries for Google.
    Credential credential = getUserCredentials(googleAuthTokenRepository);
    Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();

    // Print the first 10 task lists.
    TaskLists result = service.tasklists().list().setMaxResults(10).execute();
    List<TaskList> taskLists = result.getItems();
    if (taskLists == null || taskLists.isEmpty()) {
      System.out.println("No task lists found.");
    } else {
      System.out.println("Task lists:");
      for (TaskList tasklist : taskLists) {
        System.out.printf("%s (%s)\n", tasklist.getTitle(), tasklist.getId());
      }
    }
  }

  @Override
  public Task createTask(Task task, String taskList) {
    // TODO: Investigate how to obtain tokens from already logged users. Try to use Spring libraries for Google.
    Credential credential = getUserCredentials(googleAuthTokenRepository);
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
    }
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
