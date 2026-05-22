package com.bruno.misgastos.tasks;

import com.bruno.misgastos.dto.tasks.TaskContextDto;
import com.bruno.misgastos.exceptions.ApiException;
import jakarta.transaction.Transactional;

/**
 * Implementations of this class <strong>must</strong> define a constructor with the same parameters as the main
 * constructor of this class.
 */
public interface TaskRunner {

  @Transactional
  void execute(TaskContextDto taskContext);

  /**
   * Invoked before the {@code execute} method to validate whether a specific task can be executed.
   * This validation commonly includes checking the task configuration.
   *
   * @param taskContextDto the task context that will also be used to invoke the {@code execute} method
   * @throws ApiException if any validation fails
   */
  void validate(TaskContextDto taskContextDto) throws ApiException;
}
