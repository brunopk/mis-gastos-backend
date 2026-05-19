package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskSpringDataRepository extends JpaRepository<Task, Integer> {}
