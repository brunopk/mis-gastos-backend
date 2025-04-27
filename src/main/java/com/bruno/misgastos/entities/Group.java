package com.bruno.misgastos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"group\"")
public class Group {

  @Id
  private final Integer id;

  private final String name;

  @Column(name="category_id")
  private final Integer categoryId;

  @Column(name="subcategory_id")
  private final Integer subcategoryId;

  Group() {
    this.id = null;
    this.name = null;
    this.categoryId = null;
    this.subcategoryId = null;
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

  public Integer getSubcategoryId() {
    return subcategoryId;
  }
}
