package com.bruno.misgastos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// TODO: set "accounts" for this entity

@Entity
public class Category {

  @Id
  private final Integer id;

  private final String name;

  Category() {
    this.id = null;
    this.name = null;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }
}
