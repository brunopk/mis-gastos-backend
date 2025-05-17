package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledTaskSpringDataRepository extends JpaRepository<ScheduledTask, Integer> {}
