package com.bruno.misgastos.dto.google;

import java.time.OffsetDateTime;

public record Task(String taskId, OffsetDateTime due, String title, String notes) {
  /**
   * Use this constructor to create new tasks with {@code GoogleService}
   * ({@code src/main/java/com/bruno/misgastos/services/google/GoogleTaskService.java}). Task ID will be generated
   * automatically by Google.
   * @param due Due date for the task
   * @param title Title for the task
   * @param notes Notes (description) for the task
   */
  public Task(OffsetDateTime due, String title, String notes) {
    this(null, due, title, notes);
  }
}
