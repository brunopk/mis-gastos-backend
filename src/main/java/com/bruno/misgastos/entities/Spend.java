package com.bruno.misgastos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class Spend {

  @Id
  private final Integer id;

  private final Date date;

  @Column(name = "category_id")
  private final Integer categoryId;

  @Column(name = "subcategory_id")
  private final Integer subcategoryId;

  @Column(name = "group_id")
  private final Integer groupId;

  private final String description;

  private final Double value;

  Spend() {
    this.id = null;
    this.date = null;
    this.categoryId = null;
    this.subcategoryId = null;
    this.groupId = null;
    this.description = null;
    this.value = null;
  }

  public Integer getId() {
    return this.id;
  }

  public Date getDate() {
    return date;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public Integer getSubcategoryId() {
    return subcategoryId;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public String getDescription() {
    return description;
  }

  public Double getValue() {
    return value;
  }
}
