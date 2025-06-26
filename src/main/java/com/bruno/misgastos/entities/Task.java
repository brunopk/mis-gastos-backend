package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(name = "google_task_id")
  private final String googleTaskId;

  @Column(name = "google_task_finished_at")
  private OffsetDateTime googleTaskFinishedAt;

  @Column(name = "completed", nullable = false)
  private final Boolean completed;

  @Column(name = "spend_value", nullable = false)
  private final Double spendValue;

  @Column(name = "task_config_id", nullable = false)
  private final Integer taskConfigId;

  @Column(name = "created_at", nullable = false)
  private final OffsetDateTime createdAt;

  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Column(name = "finished_at")
  private OffsetDateTime finishedAt;

  public Task() {
    this.id = null;
    this.googleTaskId = null;
    this.googleTaskFinishedAt = null;
    this.completed = false;
    this.spendValue = null;
    this.taskConfigId = null;
    this.createdAt = null;
    this.updatedAt = null;
    this.finishedAt = null;
  }

  public Task(Integer taskConfigId, Double spendValue) {
    this.id = null;
    this.googleTaskId = null;
    this.googleTaskFinishedAt = null;
    this.completed = false;
    this.spendValue = spendValue;
    this.taskConfigId = taskConfigId;
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = OffsetDateTime.now();
    this.finishedAt = null;
  }

  public Integer getId() {
    return id;
  }

  public void setGoogleTaskFinishedAt(OffsetDateTime googleTaskFinishedAt) {
    this.googleTaskFinishedAt = googleTaskFinishedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setFinishedAt(OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }
}
