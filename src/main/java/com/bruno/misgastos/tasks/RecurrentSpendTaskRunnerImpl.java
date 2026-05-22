package com.bruno.misgastos.tasks;

import com.bruno.misgastos.dto.tasks.TaskContextDto;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.enums.TaskType;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.services.google.GoogleTasksService;
import com.bruno.misgastos.utils.ThymeleafUtils;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es"));

  private final GoogleTasksService googleTasksService;

  private final SpendSpringDataRepository spendRepository;

  private final TemplateEngine templateEngine;

  @Autowired
  public RecurrentSpendTaskRunnerImpl(GoogleTasksService googleTaskService, SpendSpringDataRepository spendRepository) {
    this.googleTasksService = googleTaskService;
    this.spendRepository = spendRepository;
    this.templateEngine = ThymeleafUtils.buildTemplateEngine();
  }

  @Override
  public void execute(TaskContextDto taskContext) {
    TaskConfig taskConfig = taskContext.task().getTaskConfig();

    switch (taskConfig.getTaskType()) {
      case AUTOMATIC ->
        processAutomaticTask(taskContext);
      case MANUAL -> processManualTask(taskContext);
    }

    boolean sendMail = taskConfig.getSendMail();
    if (sendMail) {
      // TODO: continue here
      // TODO: create the template files for mails and tasks description
    }
  }

  @Override
  public void validate(TaskContextDto taskContextDto) throws ApiException {
    TaskConfig config = taskContextDto.task().getTaskConfig();

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

  private void processAutomaticTask(TaskContextDto context) {
    Task task = context.task();
    TaskConfig taskConfig = task.getTaskConfig();
    Spend spend = buildSpend(task);
    spendRepository.save(spend);
    LOGGER.info("Spend created: {} (task_config_name={}, task_id={})", spend, taskConfig.getTaskName(), task.getId());
  }

  private void processManualTask(TaskContextDto taskContext) {
    Task task = taskContext.task();
    TaskConfig taskConfig = task.getTaskConfig();
    boolean createGoogleTask = taskConfig.getCreateGoogleTask();
    if (createGoogleTask) {
      com.bruno.misgastos.dto.google.Task googleTask = buildGoogleTask(task);
      LOGGER.info("Creating task in Google Tasks (task_config={}, task_id={})", taskConfig.getTaskName(), task.getId());
      googleTasksService.createTask(googleTask, taskContext.googleTaskList());
    }
  }

  private com.bruno.misgastos.dto.google.Task buildGoogleTask(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();
    // TODO: confirm if it's necessary to send due date in UTC
    return new com.bruno.misgastos.dto.google.Task(
        OffsetDateTime.now(), generateTaskTitle(taskConfig), generateTaskNotes(taskConfig));
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

  private String generateTaskTitle(TaskConfig taskConfig) {
    // TODO: move this method to a new GoogleTaskHelper
    String taskTitlePrefix = taskConfig.getGoogleTaskTitle();
    String formattedDate = StringUtils.capitalize(OffsetDateTime.now().format(DATE_TIME_FORMATTER));
    return String.format(
      "%s %s",
      taskTitlePrefix,
      formattedDate);
  }

  private String generateTaskNotes(TaskConfig taskConfig) {
    Context context = new Context();

    // TODO: check if server is in correct timezone (if not set it on Docker image)

    OffsetDateTime now = OffsetDateTime.now();
    context.setVariable("date", now.format((DateTimeFormatter.ISO_LOCAL_DATE)));
    context.setVariable("amount", taskConfig.getSpendValue());

    String template = taskConfig.getGoogleTaskDescriptionTemplate();
    return templateEngine.process(template, context);
  }
}
