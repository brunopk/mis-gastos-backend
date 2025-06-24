package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTask implements Runnable {

  private final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

  protected final TaskSpringDataRepository taskRepository;

  protected final TaskConfig config;

  protected AbstractTask(TaskConfig config, TaskSpringDataRepository taskRepository) {
    this.config = config;
    this.taskRepository = taskRepository;
  }

  public abstract void doWork(Task dbEntry);

  @Override
  @Transactional
  public void run() {
    try {
      Task dbEntry = new Task(config.getId(), config.getSpendValue());
      dbEntry = taskRepository.save(dbEntry);
      LOGGER.info("Starting {} (id={})", config.getTaskName(), dbEntry.getId());
      Instant start = Instant.now();

      doWork(dbEntry);

      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);
      dbEntry.setUpdatedAt(end.atOffset(ZoneOffset.UTC));
      dbEntry.setFinishedAt(end.atOffset(ZoneOffset.UTC));
      taskRepository.save(dbEntry);
      LOGGER.info(
          "{} (id={}) finished correctly in {}ms",
          config.getTaskName(),
          dbEntry.getId(),
          duration.toMillis());
    } catch (Exception ex) {
      LOGGER.error("Error executing {}", config.getTaskName(), ex);
    }
  }
}
