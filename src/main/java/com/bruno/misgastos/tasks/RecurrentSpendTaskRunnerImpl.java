package com.bruno.misgastos.tasks;

import com.bruno.misgastos.dto.rest.google.tasks.TaskDto;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.enums.TaskType;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.services.google.GoogleAuthService;
import com.bruno.misgastos.services.google.GoogleTasksService;
import com.bruno.misgastos.utils.GoogleUtils;
import com.bruno.misgastos.utils.ThymeleafUtils;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

// TODO: create a function like this ...
// https://github.com/brunopk/mis-gastos/blob/90a9be15182955c033a31ff73db2aaa4298b4593/src/Utils.ts#L301C27-L301C61

/**
 * There are two types of recurrent spend tasks:<br>
 * <br>
 * - {@code MANUAL}: generates spends automatically<br>
 * - {@code AUTOMATIC}: needs user interaction to generate spends (for example, completing the task on Google Tasks)<br>
 * <br>
 * Important considerations :<br>
 * <br>
 * - The due date for the Google Tasks will be the current date (when this handler is running).<br>
 * <br>
 */
@Component("RecurrentSpendTask")
public class RecurrentSpendTaskRunnerImpl implements TaskRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecurrentSpendTaskRunnerImpl.class);

  @Value("${mis-gastos.google.task-list-id}")
  private String GOOGLE_TASKS_TASK_LIST_ID;

  @Value("${mis-gastos.security.google.authorized-account}")
  private String AUTHORIZED_GOOGLE_ACCOUNT;

  private final GoogleAuthService googleAuthService;

  private final GoogleTasksService googleTasksService;

  private final SpendSpringDataRepository spendRepository;

  private final TemplateEngine templateEngine;

  @Autowired
  public RecurrentSpendTaskRunnerImpl(
      GoogleAuthService googleAuthService,
      GoogleTasksService googleTaskService,
      SpendSpringDataRepository spendRepository) {
    this.googleAuthService = googleAuthService;
    this.googleTasksService = googleTaskService;
    this.spendRepository = spendRepository;
    this.templateEngine = ThymeleafUtils.buildTemplateEngine();
  }

  @Override
  public void execute(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();

    switch (taskConfig.getTaskType()) {
      case AUTOMATIC -> processAutomaticTask(task);
      case MANUAL -> processManualTask(task);
    }

    boolean sendMail = taskConfig.getSendMail();
    if (sendMail) {
      // TODO: continue here
      // TODO: create the template files for mails and tasks description
    }
  }

  @Override
  public void validate(Task task) throws ApiException {
    TaskConfig config = task.getTaskConfig();

    if (config.getTaskType().equals(TaskType.AUTOMATIC) && config.getCreateGoogleTask()) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", create_google_task cannot be enabled when task_type is AUTOMATIC",
              config.getTaskName()));
    }

    if (config.getCreateGoogleTask() && Objects.isNull(config.getGoogleTaskTitle())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", google_task_title not defined",
              config.getTaskName()));
    }

    if (config.getCreateGoogleTask() && Objects.isNull(config.getGoogleTaskDescriptionTemplate())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", google_task_description_template not defined",
              config.getTaskName()));
    }

    if (config.getSendMail() && Objects.isNull(config.getMailBodyTemplate())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", mail_body_template not defined",
              config.getTaskName()));
    }

    if (config.getSendMail() && Objects.isNull(config.getMailSubject())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", mail_subject not defined",
              config.getTaskName()));
    }
  }

  private void processAutomaticTask(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();
    Spend spend = buildSpend(task);
    spendRepository.save(spend);

    // TODO: remove this (just for test)
    TaskDto googleTask = buildGoogleTasksTask(task);
    String principalName = googleAuthService.getPrincipalByEmail(AUTHORIZED_GOOGLE_ACCOUNT);
    LOGGER.info("Creating task in Google Tasks (task_config={}, task_id={})", taskConfig.getTaskName(), task.getId());
    googleTasksService.createTask(principalName, GOOGLE_TASKS_TASK_LIST_ID, googleTask);

    LOGGER.info("Spend created: {} (task_config_name={}, task_id={})", spend, taskConfig.getTaskName(), task.getId());
  }

  private void processManualTask(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();
    boolean createGoogleTask = taskConfig.getCreateGoogleTask();
    if (createGoogleTask) {
      TaskDto googleTask = buildGoogleTasksTask(task);
      String principalName = googleAuthService.getPrincipalByEmail(AUTHORIZED_GOOGLE_ACCOUNT);

      LOGGER.info("Creating task in Google Tasks (task_config={}, task_id={})", taskConfig.getTaskName(), task.getId());

      googleTasksService.createTask(principalName, GOOGLE_TASKS_TASK_LIST_ID, googleTask);
    }
  }

  private TaskDto buildGoogleTasksTask(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();
    // TODO: confirm if it's necessary to send due date in UTC
    return TaskDto.builder()
        .due(OffsetDateTime.now())
        .title(GoogleUtils.generateGoogleTasksTaskTitle(taskConfig))
        .notes(GoogleUtils.generateGoogleTasksTaskNotes(templateEngine, taskConfig))
        .build();
  }

  private Spend buildSpend(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();
    return new Spend(
      OffsetDateTime.now(),
      taskConfig.getCategoryId(),
      taskConfig.getSubcategoryId(),
      taskConfig.getSubcategoryId(),
      taskConfig.getAccountId(),
      taskConfig.getSpendDescription(),
      task.getId(),
      taskConfig.getSpendValue());
  }
}
