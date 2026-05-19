package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.TaskConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskConfigSpringDataRepository
    extends JpaRepository<TaskConfig, Integer> {}
