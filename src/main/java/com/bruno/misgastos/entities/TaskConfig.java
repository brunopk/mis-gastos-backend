package com.bruno.misgastos.entities;

import com.bruno.misgastos.enums.TaskType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class TaskConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(name = "task_name")
  private final String taskName;

  @Column(name = "task_type")
  @Enumerated(EnumType.STRING)
  private final TaskType taskType;

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
    
  public TaskConfig() {
    this.id = null;
    this.taskName = null;
    this.taskType = null;
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

  public String getTaskName() {
    return taskName;
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
