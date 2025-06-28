package com.bruno.misgastos.config;

import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.respositories.TaskConfigSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.services.google.GoogleTaskService;
import com.bruno.misgastos.tasks.AbstractTask;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
public class TaskConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskConfig.class);

  // TODO: rename scheduling to task

  @Value("${scheduling.task-executor.core-pool-size}")
  private Integer TASK_EXECUTOR_CORE_POOL_SIZE;

  @Value("${scheduling.task-executor.max-pool-size}")
  private Integer TASK_EXECUTOR_MAX_POOL_SIZE;

  @Value("${scheduling.task-executor.queue-capacity}")
  private Integer TASK_EXECUTOR_QUEUE_CAPACITY;

  @Value("${scheduling.task-scheduler.pool-size}")
  private Integer TASK_SCHEDULER_POOL_SIZE;

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(TASK_EXECUTOR_CORE_POOL_SIZE);
    executor.setMaxPoolSize(TASK_EXECUTOR_MAX_POOL_SIZE);
    executor.setQueueCapacity(TASK_EXECUTOR_QUEUE_CAPACITY);

    LOGGER.info(
        "Initializing ThreadPoolTaskExecutor with corePoolSize={} maxPoolSize={} queueCapacity={}",
        TASK_EXECUTOR_CORE_POOL_SIZE,
        TASK_EXECUTOR_MAX_POOL_SIZE,
        TASK_EXECUTOR_QUEUE_CAPACITY);

    executor.initialize();

    return executor;
  }

  @Bean
  public TaskScheduler taskScheduler(
      GoogleTaskService googleTaskService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository,
      TaskConfigSpringDataRepository taskConfigRepository) {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(1);
    taskScheduler.initialize();

    initializeTasks(
        taskScheduler,
        taskExecutor(),
        googleTaskService,
        spendRepository,
        taskRepository,
        taskConfigRepository);

    return taskScheduler;
  }

  private void initializeTasks(
      ThreadPoolTaskScheduler taskScheduler,
      ThreadPoolTaskExecutor taskExecutor,
      GoogleTaskService googleTaskService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository,
      TaskConfigSpringDataRepository taskConfigRepository) {

    LOGGER.info("Initializing scheduled tasks");

    List<com.bruno.misgastos.entities.TaskConfig> taskConfigList = taskConfigRepository.findAll();
    for (com.bruno.misgastos.entities.TaskConfig taskConfig : taskConfigList) {
      String shortClassName = taskConfig.getClassName();
      AbstractTask task =
          getTaskInstance(
              shortClassName, taskConfig, googleTaskService, spendRepository, taskRepository);

      LOGGER.info(
          "Scheduling {} with CRON expression {}",
          taskConfig.getTaskName(),
          taskConfig.getCronExpression());

      CronTrigger cronTrigger = new CronTrigger(taskConfig.getCronExpression());
      taskScheduler.schedule(
          () -> {
            taskExecutor.execute(task);
          },
          cronTrigger);
    }
  }

  private AbstractTask getTaskInstance(
      String shortClassName,
      com.bruno.misgastos.entities.TaskConfig config,
      GoogleTaskService googleTaskService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository) {
    try {
      String fullClassName = String.format("com.bruno.misgastos.tasks.%s", shortClassName);
      Class<?> clazz = Class.forName(fullClassName);
      return (AbstractTask)
          clazz
              .getConstructor(
                  com.bruno.misgastos.entities.TaskConfig.class,
                  GoogleTaskService.class,
                  SpendSpringDataRepository.class,
                  TaskSpringDataRepository.class)
              .newInstance(config, googleTaskService, spendRepository, taskRepository);
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException ex) {
      throw new RuntimeException(String.format("Error initializing %s", config.getTaskName()), ex);
    }
  }
}
