package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategorySpringDataRepository extends JpaRepository<Subcategory, Integer> {}
