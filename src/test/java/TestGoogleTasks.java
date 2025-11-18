import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class TestGoogleTasks {

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private static final List<String> SCOPES = Collections.singletonList(TasksScopes.TASKS_READONLY);

  private static final int AUTH_SERVER_PORT = 8888;

  private static final String CREDENTIALS_FILE = "test_client_id.json";

  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  // TODO: use client_id from application-local.yaml

  // TODO: CONTINUE (explain how to generate auth code flow credentials to run this test)

  @Test
  public void testListingTaskLists() throws IOException {
    Credential credential = getCredentials();
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

  private Credential getCredentials() throws IOException {
    try (InputStream is = getClass().getResourceAsStream(CREDENTIALS_FILE)) {
      if (is == null) {
        throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE);
      }
      GoogleClientSecrets clientSecrets =
          GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(is));

      // Build flow and trigger user authorization request.
      GoogleAuthorizationCodeFlow flow =
          new GoogleAuthorizationCodeFlow.Builder(
                  HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
              .setDataStoreFactory(
                  new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
              .setAccessType("offline")
              .build();
      LocalServerReceiver receiver =
          new LocalServerReceiver.Builder().setPort(AUTH_SERVER_PORT).build();
      return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
  }
}
