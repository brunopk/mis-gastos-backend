package com.bruno.misgastos.dto.google;

import java.time.OffsetDateTime;

public record Task(String taskId, OffsetDateTime due, String title, String notes) {
  /**
   * Use this constructor to create new tasks on Google Task (the id will be generated automatically by Google Tasks)
   * @param due Due date for the task
   * @param title Title for the task
   * @param notes Notes (description) for the task
   */
  public Task(OffsetDateTime due, String title, String notes) {
    this(null, due, title, notes);
  }
}
