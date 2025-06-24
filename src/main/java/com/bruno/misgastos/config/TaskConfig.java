package com.bruno.misgastos.config;

import com.bruno.misgastos.respositories.TaskConfigSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.scheduling.Scheduler;
import com.bruno.misgastos.scheduling.tasks.AbstractTask;
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

@Configuration
public class TaskConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskConfig.class);

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
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(1);
    scheduler.initialize();
    return scheduler;
  }

  @Bean
  public Scheduler scheduler(
      ThreadPoolTaskScheduler taskScheduler,
      ThreadPoolTaskExecutor taskExecutor,
      TaskSpringDataRepository taskRepository,
      TaskConfigSpringDataRepository taskConfigRepository) {
    Scheduler scheduler = new Scheduler(taskScheduler, taskExecutor);

    LOGGER.info("Initializing scheduled tasks");

    List<com.bruno.misgastos.entities.TaskConfig> taskConfigList = taskConfigRepository.findAll();
    for (com.bruno.misgastos.entities.TaskConfig taskConfig : taskConfigList) {
      String shortClassName = taskConfig.getClassName();
      AbstractTask task = getTaskInstance(shortClassName, taskConfig, taskRepository);
      LOGGER.info("Scheduling {}", taskConfig.getTaskName());
      scheduler.scheduleTask(taskConfig.getCronExpression(), task);
    }

    return scheduler;
  }

  private AbstractTask getTaskInstance(
      String shortClassName,
      com.bruno.misgastos.entities.TaskConfig config,
      TaskSpringDataRepository taskRepository) {
    try {
      String fullClassName =
          String.format("com.bruno.misgastos.scheduling.tasks.%s", shortClassName);
      Class<?> clazz = Class.forName(fullClassName);
      return (AbstractTask)
          clazz
              .getConstructor(
                  com.bruno.misgastos.entities.TaskConfig.class, TaskSpringDataRepository.class)
              .newInstance(config, taskRepository);
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException ex) {
      throw new RuntimeException(String.format("Error initializing %s", config.getTaskName()), ex);
    }
  }
}
