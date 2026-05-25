package com.bruno.misgastos.services.google;

import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;

public interface GoogleTasksService {
  /**
   * List task lists in Google Tasks for a specific Google Account identified by {@code principalName}
   * @param principalName Obtained from the {@code Authentication} object with {@code
   *     authentication.getName()}. It's used to obtain the corresponding Google access token.
   * @return Tasks list and its corresponding metadata
   */
  ListDto<TaskListDto> listTaskLists(String principalName);

  /**
   * Create a task in Google Tasks for a specific Google Account identified by {@code principalName}
   *
   * @param principalName Obtained from the {@code Authentication} object with {@code
   *     authentication.getName()}. It's used to obtain the corresponding Google access token.
   * @param taskListId Task list in which to create the task
   * @param task Task to be created
   */
  void createTask(String principalName, String taskListId, TaskDto task);
}
