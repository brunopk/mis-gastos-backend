package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.enums.TaskType;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.services.GoogleTaskService;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: if the scheduled task fails due to credentials error , the whole application should catch
// the error and finish

// TODO: create a function like this ...
// https://github.com/brunopk/mis-gastos/blob/90a9be15182955c033a31ff73db2aaa4298b4593/src/Utils.ts#L301C27-L301C61

public class RecurrentSpendTask extends AbstractTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecurrentSpendTask.class);

  public RecurrentSpendTask(
      TaskConfig config,
      GoogleTaskService googleTaskService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository) {
    super(config, googleTaskService, taskRepository, spendRepository);
  }

  @Override
  public void doWork(Task dbEntry) {
    TaskConfig taskConfig = dbEntry.getTaskConfig();
    validateTaskConfig(taskConfig);

    switch (taskConfig.getTaskType()) {
      case AUTOMATIC -> {
        Spend spend =
            new Spend(
                OffsetDateTime.now(),
                taskConfig.getCategoryId(),
                taskConfig.getSubcategoryId(),
                taskConfig.getSubcategoryId(),
                taskConfig.getAccountId(),
                taskConfig.getSpendDescription(),
                dbEntry.getId(),
                taskConfig.getSpendValue());

        spendRepository.save(spend);

        LOGGER.info("Spend created for task {}", taskConfig.getTaskName());
      }
      case MANUAL -> {
        // TODO: continue
      }
    }
  }

  public void validateTaskConfig(TaskConfig config) {
    if (config.getTaskType().equals(TaskType.AUTOMATIC) && config.getCreateGoogleTask()) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", create_google_task cannot be set while task_type is AUTOMATIC",
              config.getTaskName()));
    }

    if (config.getCreateGoogleTask() && Objects.isNull(config.getGoogleTaskTitle())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", google_task_title not defined",
              config.getTaskName()));
    }

    if (config.getCreateGoogleTask() && Objects.isNull(config.getGoogleTaskDescription())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", google_task_description not defined",
              config.getTaskName()));
    }

    if (config.getSendMail() && Objects.isNull(config.getMailBody())) {
      throw new ApiException(
          ErrorCode.INVALID_TASK_CONFIG,
          String.format(
              "Invalid task configuration for \"%s\", mail_body not defined",
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
}
