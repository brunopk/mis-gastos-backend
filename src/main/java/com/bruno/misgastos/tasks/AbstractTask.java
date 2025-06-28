package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.services.google.GoogleTaskService;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTask implements Runnable {

  private final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

  protected final GoogleTaskService googleTaskService;

  protected final TaskSpringDataRepository taskRepository;

  protected final SpendSpringDataRepository spendRepository;

  protected final TaskConfig taskConfig;

  protected AbstractTask(
      TaskConfig taskConfig,
      GoogleTaskService googleTaskService,
      TaskSpringDataRepository taskRepository,
      SpendSpringDataRepository spendRepository) {
    this.taskConfig = taskConfig;
    this.googleTaskService = googleTaskService;
    this.taskRepository = taskRepository;
    this.spendRepository = spendRepository;
  }

  public abstract void doWork(Task currentTask);

  @Override
  @Transactional
  public void run() {
    try {
      Task currentTask = new Task(taskConfig);
      currentTask = taskRepository.save(currentTask);

      LOGGER.info("Starting {} (id={})", taskConfig.getTaskName(), currentTask.getId());

      Instant start = Instant.now();

      doWork(currentTask);

      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);
      currentTask.setUpdatedAt(end.atOffset(ZoneOffset.UTC));
      currentTask.setFinishedAt(end.atOffset(ZoneOffset.UTC));
      taskRepository.save(currentTask);
      LOGGER.info(
          "{} (id={}) finished correctly in {}ms",
          taskConfig.getTaskName(),
          currentTask.getId(),
          duration.toMillis());
    } catch (Exception ex) {
      LOGGER.error("Error executing {}", taskConfig.getTaskName(), ex);
    }
  }
}
