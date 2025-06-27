package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.services.GoogleTaskService;
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

  protected final TaskConfig config;

  protected AbstractTask(
      TaskConfig config,
      GoogleTaskService googleTaskService,
      TaskSpringDataRepository taskRepository,
      SpendSpringDataRepository spendRepository) {
    this.config = config;
    this.googleTaskService = googleTaskService;
    this.taskRepository = taskRepository;
    this.spendRepository = spendRepository;
  }

  public abstract void doWork(Task dbEntry);

  @Override
  @Transactional
  public void run() {
    try {
      Task dbEntry = new Task(config);
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
