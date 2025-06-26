package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.google.Task;
import java.io.IOException;

public interface GoogleTaskService {
  // TODO: remove this method if not longer used

  void test() throws IOException;

  /**
   * Create a task
   * @param task Task to be created
   * @param taskList Task list in which to create the task
   * @throws com.bruno.misgastos.exceptions.google.GoogleApiException If an error occurs when interacting with
   *  Google Tasks API or after.
   */
  void createTask(Task task, String taskList);
}
