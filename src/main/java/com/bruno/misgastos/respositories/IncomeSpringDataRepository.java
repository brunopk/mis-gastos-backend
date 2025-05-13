package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeSpringDataRepository extends JpaRepository<Income, Integer> {}
