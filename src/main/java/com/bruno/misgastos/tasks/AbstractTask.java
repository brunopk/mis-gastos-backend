package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.services.google.GoogleMailService;
import com.bruno.misgastos.services.google.GoogleTaskService;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: create a task to process completed google tasks


/**
 * Implementations of this class <strong>must</strong> define a constructor with the same parameters as the main
 * constructor of this class.
 */
public abstract class AbstractTask implements Runnable {

  private final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

  protected final GoogleTaskService googleTaskService;

  protected final TaskSpringDataRepository taskRepository;

  protected final SpendSpringDataRepository spendRepository;

  protected final TaskConfig taskConfig;

  protected final String googleTaskListId;

  protected AbstractTask(
      String googleTaskListId,
      TaskConfig taskConfig,
      GoogleTaskService googleTaskService,
      GoogleMailService googleMailService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository) {
    this.googleTaskListId = googleTaskListId;
    this.taskConfig = taskConfig;
    this.googleTaskService = googleTaskService;
    this.taskRepository = taskRepository;
    this.spendRepository = spendRepository;
  }

  public abstract void doWork(Task taskDbEntry);

  @Override
  @Transactional
  public void run() {
    try {
      Task taskDbEntry = new Task(taskConfig);
      taskDbEntry = taskRepository.save(taskDbEntry);

      LOGGER.info("Starting task (task_name={}, task_id={})", taskConfig.getTaskName(), taskDbEntry.getId());

      Instant start = Instant.now();

      doWork(taskDbEntry);

      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);

      taskDbEntry.setUpdatedAt(end.atOffset(ZoneOffset.UTC));
      taskDbEntry.setFinishedAt(end.atOffset(ZoneOffset.UTC));

      taskRepository.save(taskDbEntry);

      LOGGER.info(
          "Task finished correctly in {}ms (task_name={}, task_id={})",
          duration.toMillis(),
          taskConfig.getTaskName(),
          taskDbEntry.getId());
    } catch (Exception ex) {
      LOGGER.error("Error executing task (task_name={})", taskConfig.getTaskName(), ex);
    }
  }
}
