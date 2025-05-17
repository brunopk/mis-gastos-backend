package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class ScheduledTask {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(name = "google_task_id")
  private final String googleTaskId;

  @Column(name = "completed", nullable = false)
  private final Boolean completed;

  @Column(name = "spend_value", nullable = false)
  private final Double spendValue;

  @Column(name = "scheduled_task_config_id", nullable = false)
  private final Integer scheduledTaskConfigId;

  @Column(name = "created_at", nullable = false)
  private final OffsetDateTime createdAt;

  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Column(name = "finished_at")
  private OffsetDateTime finishedAt;

  public ScheduledTask() {
    this.id = null;
    this.googleTaskId = null;
    this.completed = false;
    this.spendValue = null;
    this.scheduledTaskConfigId = null;
    this.createdAt = null;
    this.updatedAt = null;
    this.finishedAt = null;
  }

  public ScheduledTask(Integer scheduledTaskConfigId, Double spendValue) {
    this.id = null;
    this.googleTaskId = null;
    this.completed = false;
    this.spendValue = spendValue;
    this.scheduledTaskConfigId = scheduledTaskConfigId;
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = OffsetDateTime.now();
    this.finishedAt = null;
  }

  public Integer getId() {
    return id;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setFinishedAt(OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }
}
