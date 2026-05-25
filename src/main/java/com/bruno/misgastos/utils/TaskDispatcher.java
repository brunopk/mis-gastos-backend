package com.bruno.misgastos.utils;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.TaskConfigSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.tasks.TaskRunner;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public interface TaskDispatcher {

  Logger LOGGER = LoggerFactory.getLogger(TaskDispatcher.class);

  /**
   * Schedules all tasks defined by their corresponding {@code TaskConfig} instances, in the Spring
   * {@code TaskScheduler}. {@code TaskConfig} instances are retrieved through the {@code
   * TaskConfigSpringDataRepository}. The execution schedule, among other important configurations,
   * is defined in each instance. <br>
   * <br>
   * Scheduled tasks are also dispatched to the available threads provided by the {@code
   * TaskExecutor} at the corresponding execution time. <br>
   * <br>
   *
   * @param applicationContext Used to obtain Spring beans.
   * @param taskScheduler Used to schedule tasks.
   * @param taskExecutor Used to run tasks (at the corresponding time).
   * @param taskConfigRepository Used to obtain all defined tasks.
   * @param taskRepository Used to store task execution information.
   */
  static void initializeTasks(
      ApplicationContext applicationContext,
      TaskScheduler taskScheduler,
      TaskExecutor taskExecutor,
      TaskConfigSpringDataRepository taskConfigRepository,
      TaskSpringDataRepository taskRepository) {
    LOGGER.info("Initializing scheduled tasks");

    List<TaskConfig> taskConfigList = taskConfigRepository.findAll();
    for (TaskConfig taskConfig : taskConfigList) {
      String className = taskConfig.getClassName();
      TaskRunner taskRunner = (TaskRunner) applicationContext.getBean(className);

      LOGGER.info(
          "Scheduling task \"{}\" with CRON expression {} (task_name={})",
          taskConfig.getTaskName(),
          taskConfig.getCronExpression(),
          taskConfig.getTaskName());

      CronTrigger cronTrigger = new CronTrigger(taskConfig.getCronExpression());

      taskScheduler.schedule(
          () -> taskExecutor.execute(() -> executeTask(taskRunner, taskRepository, taskConfig)),
          cronTrigger);
    }
  }

  private static void executeTask(
      TaskRunner taskRunner, TaskSpringDataRepository taskRepository, TaskConfig taskConfig) {
    try {

      Task task = new Task(taskConfig);
      task = taskRepository.save(task);

      try {
        LOGGER.info("Validating task (task_name={}, task_id={})", taskConfig.getTaskName(), task.getId());
        taskRunner.validate(task);
      } catch (Exception ex) {
        LOGGER.error("Error validating task (task_name={})", taskConfig.getTaskName(), ex);
        return;
      }

      LOGGER.info(
          "Starting task \"{}\" (task_name={}, task_id={})",
          taskConfig.getTaskName(),
          taskConfig.getTaskName(),
          task.getId());

      Instant start = Instant.now();

      taskRunner.execute(task);

      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);

      task.setUpdatedAt(end.atOffset(ZoneOffset.UTC));
      task.setFinishedAt(end.atOffset(ZoneOffset.UTC));

      taskRepository.save(task);

      LOGGER.info(
        "Task \"{}\" finished correctly in {}ms (task_name={}, task_id={})",
        taskConfig.getTaskName(),
        duration.toMillis(),
        taskConfig.getTaskName(),
        task.getId());
    } catch (Exception ex) {
      LOGGER.error("Error executing task (task_name={})", taskConfig.getTaskName(), ex);
    }
  }
}
