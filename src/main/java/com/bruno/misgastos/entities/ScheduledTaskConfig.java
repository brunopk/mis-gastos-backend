package com.bruno.misgastos.entities;

import com.bruno.misgastos.enums.ScheduledTaskType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class ScheduledTaskConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(name = "scheduled_task_name")
  private final String scheduledTaskName;

  @Column(name = "scheduled_task_type")
  @Enumerated(EnumType.STRING)
  private final ScheduledTaskType scheduledTaskType;

  @Column(name = "class_name")
  private final String className;

  @Column(name = "create_google_task")
  private final Boolean createGoogleTask;
  
  @Column(name = "send_mail")
  private final Boolean sendMail;
  
  @Column(name = "mail_subject")
  private final String mailSubject;

  @Column(name = "mail_body")
  private final String mailBody;
  
  @Column(name = "category_id", nullable = false)
  private final Integer categoryId;

  @Column(name = "subcategory_id")
  private final Integer subcategoryId;

  @Column(name = "group_id")
  private final Integer groupId;
  
  @Column(name = "account_id", nullable = false)
  private final Integer accountId;
  
  @Column(name = "spend_value")
  private final Double spendValue;

  @Column(name = "spend_description")
  private final String spendDescription;

  @Column(name = "cron_expression", nullable = false)
  private final String cronExpression;
  
  @Column(name = "created_at", nullable = false)
  private final OffsetDateTime createdAt;

  @Column(name = "updated_at")
  private final OffsetDateTime updatedAt;
    
  public ScheduledTaskConfig() {
    this.id = null;
    this.scheduledTaskName = null;
    this.scheduledTaskType = null;
    this.className = null;
    this.createGoogleTask = null;
    this.sendMail = null;
    this.mailSubject = null;
    this.mailBody = null;
    this.categoryId = null;
    this.subcategoryId = null;
    this.groupId = null;
    this.accountId = null;
    this.spendValue = null;
    this.spendDescription = null;
    this.cronExpression = null;
    this.createdAt = null;
    this.updatedAt = null;
  }

  public Integer getId() {
    return id;
  }

  public String getScheduledTaskName() {
    return scheduledTaskName;
  }

  public String getClassName() {
    return className;
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public Double getSpendValue() {
    return spendValue;
  }
}
