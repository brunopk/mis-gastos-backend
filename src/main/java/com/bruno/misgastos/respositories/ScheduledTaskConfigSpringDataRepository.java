package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.ScheduledTaskConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledTaskConfigSpringDataRepository
    extends JpaRepository<ScheduledTaskConfig, Integer> {}
