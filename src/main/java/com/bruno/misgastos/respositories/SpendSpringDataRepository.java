package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Spend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendSpringDataRepository extends JpaRepository<Spend, Integer> {}
