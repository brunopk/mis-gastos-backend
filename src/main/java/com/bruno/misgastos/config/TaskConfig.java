package com.bruno.misgastos.config;

import com.bruno.misgastos.respositories.TaskConfigSpringDataRepository;
import com.bruno.misgastos.respositories.TaskSpringDataRepository;
import com.bruno.misgastos.utils.TaskDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskConfig.class);

  @Value("${mis-gastos.scheduling.task-executor.core-pool-size}")
  private Integer TASK_EXECUTOR_CORE_POOL_SIZE;

  @Value("${mis-gastos.scheduling.task-executor.max-pool-size}")
  private Integer TASK_EXECUTOR_MAX_POOL_SIZE;

  @Value("${mis-gastos.scheduling.task-executor.queue-capacity}")
  private Integer TASK_EXECUTOR_QUEUE_CAPACITY;

  @Value("${mis-gastos.scheduling.task-scheduler.pool-size}")
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
      ApplicationContext applicationContext,
      TaskExecutor taskExecutor,
      TaskConfigSpringDataRepository taskConfigRepository,
      TaskSpringDataRepository taskRepository) {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(1);
    taskScheduler.initialize();

    TaskDispatcher.initializeTasks(
        applicationContext,
        taskScheduler,
        taskExecutor,
        taskConfigRepository,
        taskRepository);

    return taskScheduler;
  }
}
