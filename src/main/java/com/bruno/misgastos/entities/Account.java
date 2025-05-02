package com.bruno.misgastos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// TODO: create new entities (spend_task, income, etc)

@Entity
public class Account {

  @Id
  private final Integer id;

  private final String name;

  Account() {
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
