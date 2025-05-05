package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.IncomeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeTypeSpringDataRepository extends JpaRepository<IncomeType, Integer> {}
