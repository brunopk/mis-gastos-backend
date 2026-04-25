package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.enums.TaskType;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.services.google.GoogleMailService;
import com.bruno.misgastos.services.google.GoogleTaskService;
import com.bruno.misgastos.utils.ThymeleafUtils;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

// TODO: rename to RecurrentSpendTaskHandler

// TODO: create a function like this ...
// https://github.com/brunopk/mis-gastos/blob/90a9be15182955c033a31ff73db2aaa4298b4593/src/Utils.ts#L301C27-L301C61
// TODO: consider removing category_id in group_id (as this is can be inferred by the parent subcategory)

/**
 * There are two types of recurrent spend tasks:<br>
 * <br>
 * - {@code MANUAL}: generates spends automatically<br>
 * - {@code AUTOMATIC}: needs user interaction to generate spends (for example, completing the task on Google Tasks)<br>
 * <br>
 * Important considerations :<br>
 * <br>
 * - The due date for the Google Tasks will be the actual date when this handler is running.<br>
 * <br>
 */
public class RecurrentSpendTask extends AbstractTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecurrentSpendTask.class);

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es"));

  private final TemplateEngine templateEngine;
  
  public RecurrentSpendTask(
      String googleTaskListId,
      TaskConfig taskConfig,
      GoogleTaskService googleTaskService,
      GoogleMailService googleMailService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository) {
    super(googleTaskListId, taskConfig, googleTaskService, googleMailService, spendRepository, taskRepository);
    this.templateEngine = ThymeleafUtils.buildTemplateEngine();
  }

  /**
   * Process the task.
   * @param task Represents the task that will be processed. The task configuration ({@code task.getTaskConfig()})
   *             is the same as the configuration passed to the constructor.
   */
  @Override
  @Transactional
  public void doWork(Task task) {
    validateTaskConfig(taskConfig);

    switch (taskConfig.getTaskType()) {
      case AUTOMATIC ->
        processAutomaticTask(task);
      case MANUAL ->
        processManualTask(task);
    }

    boolean sendMail = taskConfig.getSendMail();
    if (sendMail) {
      // TODO: CONTINUE here
      // TODO: create the template files for mails and tasks description
    }
  }

  private void validateTaskConfig(TaskConfig config) {
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
    LOGGER.info("Spend created: {} (task_config_name={}, task_id={})", spend, taskConfig.getTaskName(), task.getId());
  }

  private void processManualTask(Task task) {
    TaskConfig taskConfig = task.getTaskConfig();
    boolean createGoogleTask = taskConfig.getCreateGoogleTask();
    if (createGoogleTask) {
      com.bruno.misgastos.dto.google.Task googleTask = buildGoogleTask(task);
      LOGGER.info("Creating task in Google (task_config={}, task_id={})", taskConfig.getTaskName(), task.getId());
      googleTaskService.createTask(googleTask, googleTaskListId);
    }
  }

  private com.bruno.misgastos.dto.google.Task buildGoogleTask(Task misGastosTask) {
    TaskConfig taskConfig = misGastosTask.getTaskConfig();
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
