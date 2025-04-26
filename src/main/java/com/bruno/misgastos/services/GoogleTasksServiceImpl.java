package com.bruno.misgastos.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleTasksServiceImpl implements GoogleTasksService {

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private final ClassLoader classLoader;

  private GoogleTasksServiceImpl() {
    this.classLoader = getClass().getClassLoader();
  }

  @Override
  public void test() throws IOException {
    Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials()).build();

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

  /** Creates an authorized Credential object. */
  private Credential getCredentials() throws IOException {
    // load client secrets

    try (InputStream clientSecretsAsInputStream =
        classLoader.getResourceAsStream("client_secret.json")) {
      if (clientSecretsAsInputStream == null) throw new IOException("Cannot load client secret");
      GoogleClientSecrets clientSecrets =
          GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(clientSecretsAsInputStream));

      // generate credentials for the user
      GoogleAuthorizationCodeFlow authCodeFlow =
          new GoogleAuthorizationCodeFlow.Builder(
                  HTTP_TRANSPORT,
                  JSON_FACTORY,
                  clientSecrets,
                  Collections.singleton(TasksScopes.TASKS))
              .setDataStoreFactory(
                  new FileDataStoreFactory(new java.io.File("./")))
              .build();

      // TODO: get the correct port depending on environment

      // authorize
      LocalServerReceiver receiver =
          new LocalServerReceiver.Builder()
              .setPort(8081)
              .setCallbackPath("/google/redirect/oauth")
              .build();

      return new AuthorizationCodeInstalledApp(authCodeFlow, receiver).authorize("user");
    }
  }
}
