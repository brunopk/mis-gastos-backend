package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Spend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(nullable = false)
  private final OffsetDateTime date;

  @Column(name = "category_id", nullable = false)
  private final Integer categoryId;

  @Column(name = "subcategory_id")
  private final Integer subcategoryId;

  @Column(name = "group_id")
  private final Integer groupId;

  @Column(name = "account_id", nullable = false)
  private final Integer accountId;

  private final String description;

  @Column(nullable = false)
  private final Double value;

  public Spend(
      OffsetDateTime date,
      Integer categoryId,
      Integer subcategoryId,
      Integer groupId,
      Integer accountId,
      String description,
      double value) {
    this.id = null;
    this.date = date;
    this.categoryId = categoryId;
    this.subcategoryId = subcategoryId;
    this.groupId = groupId;
    this.accountId = accountId;
    this.description = description;
    this.value = value;
  }

  Spend() {
    this.id = null;
    this.date = null;
    this.categoryId = null;
    this.subcategoryId = null;
    this.groupId = null;
    this.accountId = null;
    this.description = null;
    this.value = null;
  }

  public Integer getId() {
    return this.id;
  }

  public OffsetDateTime getDate() {
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

  public Integer getAccountId() {
    return accountId;
  }

  public String getDescription() {
    return description;
  }

  public Double getValue() {
    return value;
  }
}
