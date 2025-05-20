package com.bruno.misgastos.services;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleTasksServiceImpl implements GoogleTasksService {

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private final GoogleAuthService googleAuthService;

  @Autowired
  public GoogleTasksServiceImpl(GoogleAuthService googleAuthService) {
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
}
