package com.bruno.misgastos.rest.google;

import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;

public interface GoogleTasksApiRestClient {
  ListDto<TaskListDto> listTaskLists(String accessToken);

  /**
   * Create a task into the task list identified by {@code taskListId}.
   * @param accessToken Used to interact with Google Tasks API.
   * @param taskListId Identifies the task list in which to create the task.
   * @param task task to be created.
   */
  TaskDto createTask(String accessToken, String taskListId, TaskDto task);
}
