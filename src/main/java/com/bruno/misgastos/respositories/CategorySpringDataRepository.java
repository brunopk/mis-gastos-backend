package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorySpringDataRepository extends JpaRepository<Category, Integer> {}
