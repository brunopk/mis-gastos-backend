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

  @Column(name = "is_google_task_completed")
  private final Boolean isGoogleTaskCompleted;

  @Column(name = "spend_value")
  private final Double spendValue;

  @ManyToOne(optional = false)
  @JoinColumn(name = "task_config_id", nullable = false)
  private final TaskConfig taskConfig;

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
    this.isGoogleTaskCompleted = false;
    this.spendValue = null;
    this.taskConfig = null;
    this.createdAt = null;
    this.updatedAt = null;
    this.finishedAt = null;
  }

  public Task(TaskConfig config) {
    this.id = null;
    this.googleTaskId = null;
    this.googleTaskFinishedAt = null;
    this.isGoogleTaskCompleted = false;
    this.spendValue = config.getSpendValue();
    this.taskConfig = config;
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = OffsetDateTime.now();
    this.finishedAt = null;
  }

  public Integer getId() {
    return id;
  }

  public TaskConfig getTaskConfig() {
    return taskConfig;
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
