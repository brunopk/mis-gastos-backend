package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Subcategory {

  @Id private final Integer id;

  private final String name;

  @Column(name = "category_id")
  private final Integer categoryId;

  @ManyToMany
  @JoinTable(
      name = "subcategory_account",
      joinColumns = @JoinColumn(name = "subcategory_id"),
      inverseJoinColumns = @JoinColumn(name = "account_id"))
  private final List<Account> accounts;

  Subcategory() {
    this.id = null;
    this.name = null;
    this.categoryId = null;
    this.accounts = null;
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

  public List<Account> getAccounts() {
    return accounts;
  }
}
