package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: continue

// TODO: if the scheduled task fails due to credentials error , the whole application should catch the error and finish

public class RecurrentSpendsTask extends AbstractTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecurrentSpendsTask.class);

  public RecurrentSpendsTask(
    TaskConfig config, TaskSpringDataRepository scheduledTaskRepository) {
    super(config, scheduledTaskRepository);
  }

  @Override
  public void doWork(Task dbEntry) {
    LOGGER.debug("Executing RecurrentSpendsTask");
  }
}
