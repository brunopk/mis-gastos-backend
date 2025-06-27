package com.bruno.misgastos.dto.google;

import java.time.OffsetDateTime;

public record Task(String taskId, OffsetDateTime due, String title, String notes) {
  public Task(OffsetDateTime due, String title, String notes) {
    this(null, due, title, notes);
  }
}
