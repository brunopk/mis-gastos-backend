package com.bruno.misgastos.rest.google;

import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;

public interface GoogleTasksApiRestClient {
  ListDto<TaskListDto> listTaskLists(String accessToken);
}
