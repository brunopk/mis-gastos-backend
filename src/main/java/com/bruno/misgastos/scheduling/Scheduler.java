package com.bruno.misgastos.scheduling;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.support.CronTrigger;

public class Scheduler {

  private final TaskScheduler taskScheduler;

  private final ThreadPoolTaskExecutor taskExecutor;

  public Scheduler(TaskScheduler taskScheduler, ThreadPoolTaskExecutor taskExecutor) {
    this.taskScheduler = taskScheduler;
    this.taskExecutor = taskExecutor;
  }

  public void scheduleTask(String cronExpression, Runnable task) {
    CronTrigger cronTrigger = new CronTrigger(cronExpression);

    taskScheduler.schedule(
        () -> {
          taskExecutor.execute(task);
        },
        cronTrigger);
  }
}
