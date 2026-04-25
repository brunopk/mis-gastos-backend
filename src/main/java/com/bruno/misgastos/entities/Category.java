package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Category {

  @Id private final Integer id;

  private final String name;

  @ManyToMany
  @JoinTable(
      name = "category_account",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "account_id"))
  private final List<Account> accounts;

  Category() {
    this.id = null;
    this.name = null;
    this.accounts = null;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public List<Account> getAccounts() {
    return accounts;
  }
}
