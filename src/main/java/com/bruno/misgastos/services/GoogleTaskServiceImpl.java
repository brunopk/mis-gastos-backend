package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.google.Task;
import com.bruno.misgastos.exceptions.google.GoogleApiException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleTaskServiceImpl implements GoogleTaskService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTaskServiceImpl.class);

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private final GoogleAuthService googleAuthService;

  @Autowired
  public GoogleTaskServiceImpl(GoogleAuthService googleAuthService) {
    this.googleAuthService = googleAuthService;
  }

  @Override
  public void test() throws IOException{
    Credential credential = googleAuthService.getUserCredentials();
    
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

  // TODO: if necessary, consider returning task (task dto)

  @Override
  public void createTask(Task task, String taskList) {
    Credential credential = googleAuthService.getUserCredentials();
    Tasks.TasksOperations tasksOperations = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
      .build()
      .tasks();

    // Google Tasks does not support time-based due dates
    com.google.api.services.tasks.model.Task taskAux =
        new com.google.api.services.tasks.model.Task()
            .setDue(task.due().format(DateTimeFormatter.ISO_DATE_TIME))
            .setTitle(task.title())
            .setNotes(task.notes());

    try {
      LOGGER.debug("Creating task in Google");
      tasksOperations.insert(taskList, taskAux).execute();
    } catch (IOException ex) {
      throw new GoogleApiException(ex);
    }
  }
}
