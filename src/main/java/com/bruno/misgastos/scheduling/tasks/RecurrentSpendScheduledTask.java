package com.bruno.misgastos.scheduling.tasks;

import com.bruno.misgastos.entities.ScheduledTask;
import com.bruno.misgastos.entities.ScheduledTaskConfig;
import com.bruno.misgastos.respositories.ScheduledTaskSpringDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: continue

// TODO: if the scheduled task fails due to credentials error , the whole application should catch the error and finish

// TODO: rename to RecurrentSpendsTask

public class RecurrentSpendScheduledTask extends AbstractScheduledTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecurrentSpendScheduledTask.class);

  public RecurrentSpendScheduledTask(
      ScheduledTaskConfig config, ScheduledTaskSpringDataRepository scheduledTaskRepository) {
    super(config, scheduledTaskRepository);
  }

  @Override
  public void doWork(ScheduledTask dbEntry) {
    LOGGER.debug("Executing RecurrentSpendScheduledTask");
  }
}
