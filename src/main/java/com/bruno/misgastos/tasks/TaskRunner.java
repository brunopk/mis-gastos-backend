package com.bruno.misgastos.tasks;

import com.bruno.misgastos.entities.Task;
import com.bruno.misgastos.exceptions.ApiException;
import jakarta.transaction.Transactional;

/**
 * Run task defined by their corresponding {@code TaskConfig} instances
 */
public interface TaskRunner {

  /**
   * Execute a task
   * @param task Task that will be executed. Used as the context for the execution.
   */
  @Transactional
  void execute(Task task);

  /**
   * Invoked before the {@code execute} method to validate whether a specific task can be executed.
   * This validation commonly includes checking the task configuration.
   *
   * @param task Task that will be validated an executed. Used as the context for the validation.
   * @throws ApiException if any validation fails
   */
  void validate(Task task) throws ApiException;
}
