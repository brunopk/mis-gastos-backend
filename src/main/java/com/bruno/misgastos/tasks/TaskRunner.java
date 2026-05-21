package com.bruno.misgastos.tasks;

import com.bruno.misgastos.dto.tasks.TaskContextDto;
import jakarta.transaction.Transactional;

// TODO: create a task to process completed google tasks


/**
 * Implementations of this class <strong>must</strong> define a constructor with the same parameters as the main
 * constructor of this class.
 */
public interface TaskRunner {

  /*private final Logger LOGGER = LoggerFactory.getLogger(Task.class);

  protected final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

  protected final GoogleTasksService googleTaskService;

  protected final GoogleMailService googleMailService;

  protected final TaskSpringDataRepository taskRepository;

  protected final SpendSpringDataRepository spendRepository;

  protected final TaskConfig taskConfig;

  protected Task(
      OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
      TaskConfig taskConfig,
      GoogleTasksService googleTaskService,
      GoogleMailService googleMailService,
      SpendSpringDataRepository spendRepository,
      TaskSpringDataRepository taskRepository) {
    this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    this.taskConfig = taskConfig;
    this.googleTaskService = googleTaskService;
    this.googleMailService = googleMailService;
    this.taskRepository = taskRepository;
    this.spendRepository = spendRepository;
  }*/

  //public abstract void doWork(com.bruno.misgastos.entities.Task taskDbEntry);

  /*@Override
  @Transactional
  public void run() {
    try {
      com.bruno.misgastos.entities.Task taskDbEntry = new com.bruno.misgastos.entities.Task(taskConfig);
      taskDbEntry = taskRepository.save(taskDbEntry);

      LOGGER.info("Starting task (task_name={}, task_id={})", taskConfig.getTaskName(), taskDbEntry.getId());

      Instant start = Instant.now();

      doWork(taskDbEntry);

      Instant end = Instant.now();
      Duration duration = Duration.between(start, end);

      taskDbEntry.setUpdatedAt(end.atOffset(ZoneOffset.UTC));
      taskDbEntry.setFinishedAt(end.atOffset(ZoneOffset.UTC));

      taskRepository.save(taskDbEntry);

      LOGGER.info(
          "Task finished correctly in {}ms (task_name={}, task_id={})",
          duration.toMillis(),
          taskConfig.getTaskName(),
          taskDbEntry.getId());
    } catch (Exception ex) {
      LOGGER.error("Error executing task (task_name={})", taskConfig.getTaskName(), ex);
    }
  }*/

  @Transactional
  void execute(TaskContextDto taskContext);
}
