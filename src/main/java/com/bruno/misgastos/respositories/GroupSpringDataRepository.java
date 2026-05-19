package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupSpringDataRepository extends JpaRepository<Group, Integer> {}
