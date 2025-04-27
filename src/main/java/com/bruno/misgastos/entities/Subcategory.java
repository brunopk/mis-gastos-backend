package com.bruno.misgastos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Subcategory {

  @Id
  private final Integer id;

  private final String name;

  @Column(name="category_id")
  private final Integer categoryId;

  Subcategory() {
    this.id = null;
    this.name = null;
    this.categoryId = null;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public Integer getCategoryId() {
    return categoryId;
  }
}
