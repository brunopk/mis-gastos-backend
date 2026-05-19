package com.bruno.misgastos.services.google;

import com.bruno.misgastos.dto.google.Task;
import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;


public interface GoogleTasksService {
  /**
   * List task lists using Google Tasks API.
   * <br>
   * Refer to
   * <a href="https://developers.google.com/workspace/tasks/reference/rest/v1/tasks/list">Method: tasks.list</a> for
   * more information
   * @param principalName Obtained from the {@code Authentication} object with {@code authentication.getName()}.
   * @return Tasks list and its corresponding metadata
   */
  ListDto<TaskListDto> listTaskLists(String principalName);

  /**
   * Create a task
   * @param task Task to be created
   * @param taskList Task list in which to create the task
   * @return Return created task
   * @throws com.bruno.misgastos.exceptions.google.GoogleApiException If an error occurs when interacting with
   *  Google Tasks API or after.
   */
  Task createTask(Task task, String taskList);
}
