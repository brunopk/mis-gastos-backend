package com.bruno.misgastos.scheduling.tasks;

import com.bruno.misgastos.entities.ScheduledTask;
import com.bruno.misgastos.entities.ScheduledTaskConfig;
import com.bruno.misgastos.respositories.ScheduledTaskSpringDataRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: rename to AbstractTask
public abstract class AbstractScheduledTask implements Runnable {

  private final Logger LOGGER = LoggerFactory.getLogger(AbstractScheduledTask.class);

  protected final ScheduledTaskSpringDataRepository scheduledTaskRepository;

  protected final ScheduledTaskConfig config;

  protected AbstractScheduledTask(
      ScheduledTaskConfig config, ScheduledTaskSpringDataRepository scheduledTaskRepository) {
    this.config = config;
    this.scheduledTaskRepository = scheduledTaskRepository;
  }

  public abstract void doWork(ScheduledTask dbEntry);

  @Override
  @Transactional
  public void run() {
    try {
      ScheduledTask dbEntry = new ScheduledTask(config.getId(), config.getSpendValue());
      dbEntry = scheduledTaskRepository.save(dbEntry);
      LOGGER.info("Starting {} (id={})", config.getScheduledTaskName(), dbEntry.getId());
      Instant start = Instant.now();

      doWork(dbEntry);

      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);
      dbEntry.setUpdatedAt(end.atOffset(ZoneOffset.UTC));
      dbEntry.setFinishedAt(end.atOffset(ZoneOffset.UTC));
      scheduledTaskRepository.save(dbEntry);
      LOGGER.info(
          "{} (id={}) finished correctly in {}ms",
          config.getScheduledTaskName(),
          dbEntry.getId(),
          duration.toMillis());
    } catch (Exception ex) {
      LOGGER.error("Error executing {}", config.getScheduledTaskName(), ex);
    }
  }
}
