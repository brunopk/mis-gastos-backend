package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.entities.TaskConfig;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: continue

// TODO: if the scheduled task fails due to credentials error , the whole application should catch
// the error and finish

// TODO: rename to RecurrentSpendTask

// TODO: create a function like this ...
// https://github.com/brunopk/mis-gastos/blob/90a9be15182955c033a31ff73db2aaa4298b4593/src/Utils.ts#L301C27-L301C61

// TODO: create a spend (if type is automatic) like this ...
// https://github.com/brunopk/mis-gastos/blob/90a9be15182955c033a31ff73db2aaa4298b4593/src/Main.ts#L81

// TODO: if type is "automatic" completed should be set on "true"

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
